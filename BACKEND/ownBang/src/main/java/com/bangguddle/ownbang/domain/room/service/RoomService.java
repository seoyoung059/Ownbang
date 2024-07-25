package com.bangguddle.ownbang.domain.room.service;

import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomSelectResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    // 매물 생성
    SuccessResponse<NoneResponse> createRoom(RoomCreateRequest roomCreateRequest, List<MultipartFile> roomImageFiles);

    // 매물 삭제
    SuccessResponse<NoneResponse> deleteRoom(Long roomId);

    // 매물 조회
    SuccessResponse<RoomSelectResponse> getRoom(Long id);
}
