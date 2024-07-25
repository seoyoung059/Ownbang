package com.bangguddle.ownbang.domain.room.service;

import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    // 매물 생성
    SuccessResponse<NoneResponse> createRoom(RoomCreateRequest roomCreateRequest, List<MultipartFile> roomImageFiles);

    SuccessResponse<NoneResponse> deleteRoom(Long roomId);

}
