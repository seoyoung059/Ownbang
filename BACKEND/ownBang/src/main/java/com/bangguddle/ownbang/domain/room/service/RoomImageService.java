package com.bangguddle.ownbang.domain.room.service;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

public interface RoomImageService {

    SuccessResponse<NoneResponse> uploadImageToS3(MultipartFile roomImage, Room room) throws AppException;

    SuccessResponse<NoneResponse> deleteImage(Long roomId, Long roomImageId) throws AppException;
}
