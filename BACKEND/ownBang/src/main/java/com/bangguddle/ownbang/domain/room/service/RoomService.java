package com.bangguddle.ownbang.domain.room.service;

import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomInfoSearchResponse;
import com.bangguddle.ownbang.domain.room.dto.RoomSearchResponse;
import com.bangguddle.ownbang.domain.room.dto.RoomUpdateRequest;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    // 매물 생성
    SuccessResponse<NoneResponse> createRoom(Long userId, RoomCreateRequest request, List<MultipartFile> roomImageFiles);

    // 매물 수정
    SuccessResponse<NoneResponse> modifyRoom(Long userId, Long roomId, RoomUpdateRequest request, List<MultipartFile> roomImageFiles) ;


    // 매물 삭제
    SuccessResponse<NoneResponse> deleteRoom(Long userId, Long roomId);

    // 매물 단건 조회
    SuccessResponse<RoomSearchResponse> getRoom(Long roomId);

    // 중개인의 매물 목록 조회
    SuccessResponse<List<RoomInfoSearchResponse>> getAgentRooms(Long userId, int page, int size);

    SuccessResponse<List<RoomInfoSearchResponse>> search();
}
