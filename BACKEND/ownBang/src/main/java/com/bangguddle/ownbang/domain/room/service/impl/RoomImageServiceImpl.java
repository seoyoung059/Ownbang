package com.bangguddle.ownbang.domain.room.service.impl;


import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomImage;
import com.bangguddle.ownbang.domain.room.repository.RoomImageRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.ROOM_DELETE_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.ROOM_IMAGE_UPLOAD_SUCCESS;

@Service
@RequiredArgsConstructor
public class RoomImageServiceImpl {

    private final RoomImageRepository roomImageRepository;

    /**
     * 매물 이미지 업로드를 위한 서비스 메서드
     * @param roomImage MultipartFile 이미지 파일
     * @param room 이미지를 업로드할 매물
     * @return Success Response
     * @throws AppException 매물 파일 저장 실패 시 AppException(IMAGE_UPLOAD_FAILED) 발생
     */
    @Transactional
    public SuccessResponse<NoneResponse> uploadImage(MultipartFile roomImage, Room room) throws AppException {
        validateImageFile(roomImage);
        //파일명: UUID + 사진 원래이름
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + roomImage.getOriginalFilename();

        //실제 저장 경로
        String dbFilePath = "/upload/roomImages/" + fileName;
        Path filePath = Paths.get("src/main/resources/static", dbFilePath);

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, roomImage.getBytes());
        } catch (IOException e) {
            throw new AppException(IMAGE_UPLOAD_FAILED);
        }
        room.getRoomImages().add(new RoomImage(room, dbFilePath));

        return new SuccessResponse<>(ROOM_IMAGE_UPLOAD_SUCCESS, NoneResponse.NONE);
    }


    /**
     * 매물 이미지 삭제 서비스 메서드
     * @param roomId 유효성 확인을 위한 매물 ID
     * @param roomImageId 삭제할 매물 이미지 ID
     * @return SuccessResponse
     * @throws AppException 매물 이미지 삭제 실패 시 IMAGE_DELETE_FAILED 발생
     */
    @Transactional
    public SuccessResponse<NoneResponse> deleteImage(Long roomId, Long roomImageId) throws AppException {
        RoomImage roomImage = roomImageRepository.findById(roomImageId)
                .orElseThrow(() -> new AppException(INVALID_IMAGE_FILE));
        if(!roomImage.getRoom().getId().equals(roomId)) {
            throw new AppException(INVALID_IMAGE_FILE);
        }


        String dbFilePath = roomImage.getRoomImageUrl();
        Path filePath = Paths.get("src/main/resources/static", dbFilePath);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new AppException(IMAGE_DELETE_FAILED);
        }

        roomImageRepository.delete(roomImage);

        return new SuccessResponse<>(ROOM_DELETE_SUCCESS, NoneResponse.NONE);
    }

    /**
     * 이미지 파일 유효성 검사
     * @param roomImage MultipartFile 형식의 이미지 파일
     * @throws AppException 이미지 파일이 아니거나 적절한 형식이 아닐 때 INVALID_IMAGE_FILE 예외 발생
     */
    private void validateImageFile(MultipartFile roomImage) throws AppException {
        String contentType = roomImage.getContentType();
        if (contentType == null || !contentType.contains("image")) {
            throw new AppException(INVALID_IMAGE_FILE);
        }
    }
}
