package com.bangguddle.ownbang.domain.streaming.service.impl;


import com.bangguddle.ownbang.domain.streaming.service.StreamingService;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.bangguddle.ownbang.global.service.impl.S3UploaderServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StreamingServiceImpl implements StreamingService {


    private final ObjectMapper objectMapper;
    private final FFmpeg fFmpeg;
    private final FFprobe fFprobe;
    private final S3UploaderServiceImpl s3UploaderService;

    private static final String zipExtend = ".zip";
    private static final String m3u8Extend = ".m3u8";
    private static final String tsExtend = "_%08d.ts";
    private static final String jsonExtend = ".json";

    @Value("${video.recordings.path}")
    private String reocrdingPath;


    /**
     * sessionId를 받아 zip 압축 해제 후 hls로 변환하여 S3에 올림
     * @param sessionId
     * @return
     */
    @Override
    public SuccessResponse<String> uploadStreaming(String sessionId){
        log.info("uploadStreaming sessionId:{}", sessionId);

        Path outputPath = Paths.get(reocrdingPath, sessionId);
        log.info(outputPath.toAbsolutePath().toString());

        // json에서 publisher(중개인)의 녹화 파일명을 얻는다.
        String filename = getPublisherFileName(outputPath.toString(), sessionId);
        if(filename == null) throw new AppException(RECORDING_ERROR);

        // 압축 해제한 파일이 zip파일과 같은 위치여야 openvidu의 delete기능을 사용할 수 있다.
        String unzipFileName = unzipFile(outputPath.toString(), sessionId, filename);

        // 압축 파일들을 hls로 변환한다.
        convertToHls(unzipFileName, sessionId);

        // s3에 업로드한다.
        String uploadedUrl = s3UploaderService.uploadHlsFiles(Paths.get(outputPath.toString(),sessionId), sessionId);

        return new SuccessResponse<>(SuccessCode.ROOM_IMAGE_UPLOAD_SUCCESS, uploadedUrl);
    }

    /**
     * Openvidu 녹화 시 함께 생성되는 json파일을 파싱하여 publisher(중개인)의 녹화 파일명을 얻는 메서드
     * @param outputPath
     * @param sessionId
     * @return publisher(중개인)의 녹화 파일명
     */
    private String getPublisherFileName(String outputPath, String sessionId) {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(outputPath, sessionId + jsonExtend)));
            JsonNode filesNode = objectMapper.readTree(jsonString).path("files");

            if (filesNode.isArray() && !filesNode.isEmpty()) {
                JsonNode firstFile = filesNode.get(0);
                return firstFile.path("name").asText();
            }

            return null; // 파일이 없거나 name 필드가 없는 경우
        } catch (Exception e) {
            // 예외 처리: 로깅을 하거나 사용자 정의 예외를 던질 수 있습니다.
            System.out.println(e.getMessage());
            throw new AppException(RECORDING_ERROR);
        }
    }


    /**
     * 압축 파일에서 publisher(중개인)의 녹화영상만을 sessionId를 파일명으로 압축 해제합니다.
     * @param outputPath
     * @param sessionId
     * @param publisherFileName 중개인 녹화영상 파일명(확장자 포함)
     * @return 압축 해제한 파일명 (sessionId.확장자)
     */
    private String unzipFile(String outputPath, String sessionId, String publisherFileName) {
        try {
            File zipFile = new File(outputPath , sessionId + zipExtend);
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                if (!ze.getName().contains(publisherFileName)) {
                    ze = zis.getNextEntry();
                    continue;
                }

                String recordFileName = sessionId + "."+ze.getName().split("\\.")[1];
                File recordFile = new File(outputPath, recordFileName);

                recordFile.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(recordFile);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zis.closeEntry();
                zis.close();
                return recordFileName;
            }
            throw new AppException(RECORDING_ERROR); // 파일 없음
        } catch (Exception e) {
            throw new AppException(RECORDING_ERROR);  // 예외 발생
        }
    }


    /**
     * fileName의 녹화영상을 HLS 포맷으로 sessionId 폴더에 압축 해제한다.
     * @param filename 변환할 녹화영상의 파일명 (sessionId+확장자)
     * @param sessionId
     * @return m3u8 파일의 파일명
     */
    private String convertToHls(String filename, String sessionId) {
        try {
            String inputFilePath = Paths.get(reocrdingPath, sessionId, filename).toAbsolutePath().toString();
            File output = new File(Paths.get(reocrdingPath, sessionId,sessionId).toAbsolutePath().toString());

            // 파일 없을 때
            if(!(new File(inputFilePath)).exists()) throw new FileNotFoundException(filename);
            // 경로 생성
            if(!output.exists()) output.mkdirs();

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputFilePath)
                    .overrideOutputFiles(true)
                    .addOutput(Paths.get(output.getAbsolutePath() ,sessionId+m3u8Extend).toAbsolutePath().toString())
                    .setFormat("hls")
                    .addExtraArgs("-hls_time", "10")
                    .addExtraArgs("-hls_list_size", "0")
                    .addExtraArgs("-hls_segment_filename", output.getAbsolutePath() + File.separator + sessionId + tsExtend) // 청크 파일 이름
                    .addExtraArgs("-c:v", "libx264")
                    .addExtraArgs("-preset", "faster")
                    .addExtraArgs("-crf", "23")
                    .addExtraArgs("-c:a", "aac")
                    .addExtraArgs("-b:a", "128k")
                    .done();

            run(builder, filename);
            return filename + m3u8Extend;
        } catch (FileNotFoundException e){
            throw new AppException(HLS_CONVERTING_ERROR);
        }

    }

    /**
     * HLS 변환 수행 메서드
     * @param builder FFMPEG Builder
     * @param filename hls 변환 결과의 파일명
     */
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
