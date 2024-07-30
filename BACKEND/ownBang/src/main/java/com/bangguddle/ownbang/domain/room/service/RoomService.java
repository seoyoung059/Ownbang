package com.bangguddle.ownbang.domain.room.service;

import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomListSearchResponse;
import com.bangguddle.ownbang.domain.room.dto.RoomSearchResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    // 매물 생성
    SuccessResponse<NoneResponse> createRoom(RoomCreateRequest roomCreateRequest, List<MultipartFile> roomImageFiles);

    // 매물 삭제
    SuccessResponse<NoneResponse> deleteRoom(Long roomId);

    // 매물 조회
    SuccessResponse<RoomSearchResponse> getRoom(Long id);

    // 매물 전체 조회
    SuccessResponse<List<RoomListSearchResponse>> getAllRooms();
}
