package com.bangguddle.ownbang.domain.streaming.service.impl;


import com.bangguddle.ownbang.global.handler.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.progress.Progress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.bangguddle.ownbang.global.enums.ErrorCode.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class StreamingServiceImpl {


    private final FFmpeg fFmpeg;
    private final FFprobe fFprobe;

    @Value("${video.recordings.path}")
    private String reocrdingPath;

    // zip파일 위치, 중개인 토큰명, reservationId 받아 압축 해제, hls 변환, s3 업로드 하고, 완료 후 m3u8의 uri를 return하는 부분
    public /*SuccessResponse<StreamingDto>*/ void uploadStreaming(String sessionId, String agentToken, Long reservationId){
        // 압축 해제한 파일이 zip파일과 같은 위치여야 openvidu의 delete기능을 사용할 수 있다.
        String unzipFileName = unzipFile(sessionId, agentToken, reservationId);

        // 압축 파일들을 hls로 변환한다.
        convertToHls(unzipFileName, sessionId);

        // s3에 업로드한다.
        // TODO : S3 서비스 없어!

        return;
    }

    // mp4 압축 해제 메서드 (zip파일 위치, 저장 위치)
    private String unzipFile(String sessionId, String agentToken, Long reservationId) {
        String fileName = "";
        try {
            String zipPath = reocrdingPath + File.separator + sessionId + File.separator + sessionId + ".zip";
            String outputPath = reocrdingPath + File.separator + sessionId + File.separator;
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                if (!ze.getName().contains(agentToken)) {
                    ze = zis.getNextEntry();
                    continue;
                }
                File recordFile = new File(outputPath + File.separator + ze.getName());

                recordFile.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(recordFile);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                fileName = ze.getName();
            }
            zis.closeEntry();
            zis.close();
        } catch (Exception e) {
            throw new AppException(INTERNAL_SERVER_ERROR);
        }
        return fileName;
    }

    // mp4 파일 hls로 변환 (mp4 파일 위치)
    private void convertToHls(String filename, String sessionId) {
        try {
            String inputFilePath = Paths.get(reocrdingPath, sessionId, filename).toAbsolutePath().toString();
            File inputFile = new File(inputFilePath);
            File output = new File(Paths.get(reocrdingPath + File.separator+filename.split("\\.")[0]).toAbsolutePath().toString());
            // 파일 없을 때
            if(!inputFile.exists()) throw new FileNotFoundException(filename);
            // 경로 생성
            if(!output.exists()) output.mkdirs();

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputFilePath)
                    .overrideOutputFiles(true)
                    .addOutput(output.getAbsolutePath() + "/"+filename+".m3u8")
                    .setFormat("hls")
                    .addExtraArgs("-hls_time", "10")
                    .addExtraArgs("-hls_list_size", "0")
                    .addExtraArgs("-hls_segment_filename", output.getAbsolutePath() + "/" + filename + "master_%08d.ts") // 청크 파일 이름
                    .done();

            run(builder, filename);
        } catch (FileNotFoundException e){
            throw new AppException(INTERNAL_SERVER_ERROR);
        }

    }


    private void run(FFmpegBuilder builder, String filename) {
        FFmpegExecutor executor = new FFmpegExecutor(fFmpeg, fFprobe);
        executor
                .createJob(builder, progress -> {
                    if (progress.status.equals(Progress.Status.END)) {
                        log.info("============================ JOB FINISHED {}============================", filename);
                    }
                })
                .run();
    }


}
