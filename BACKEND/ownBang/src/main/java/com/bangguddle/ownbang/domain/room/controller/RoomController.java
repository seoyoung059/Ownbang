package com.bangguddle.ownbang.domain.room.controller;

import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomSearchResponse;
import com.bangguddle.ownbang.domain.room.dto.RoomUpdateRequest;
import com.bangguddle.ownbang.domain.room.service.RoomService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    /**
     * 매물 생성
     * @param request 매물 정보 JSON
     * @param roomImageFiles 매물 관련 이미지 파일
     * @return Success Response, 실패 시 AppException Throw
     */
    @PostMapping("/agents")
    public ResponseEntity<Response<NoneResponse>> addRoom(@AuthenticationPrincipal Long userId,
                                                          @RequestPart(value = "roomCreateRequest") @Valid RoomCreateRequest request,
                                                          @RequestPart(value = "roomImageFiles", required = false) List<MultipartFile> roomImageFiles) {
        return Response.success(roomService.createRoom(userId, request, roomImageFiles));
    }

    /**
     * 매물 정보 수정
     * @param roomId 수정할 매물의 ID
     * @param request 수정할 매물의 정보 JSON
     * @param roomImageFiles 매물 관련 새로 업로드할 이미지 파일
     * @return Success Response, 실패 시 AppException Throw
     */
    @PatchMapping("agents/{roomId}")
    public ResponseEntity<Response<NoneResponse>> updateRoom(@AuthenticationPrincipal Long userId,
                                                             @PathVariable(value = "roomId") @Valid @Positive Long roomId,
                                                             @RequestPart(value="roomUpdateRequest") @Valid RoomUpdateRequest request,
                                                             @RequestPart(value="roomImageFiles", required = false) List<MultipartFile> roomImageFiles) {
        return Response.success(roomService.updateRoom(userId, roomId, request, roomImageFiles));
    }

    /**
     * 매물 삭제
     * @param roomId 삭제할 매물의 ID
     * @return Success Response, 실패 시 AppException Throw
     */
    @DeleteMapping("agents/{roomId}")
    public ResponseEntity<Response<NoneResponse>> deleteRoom(@AuthenticationPrincipal Long userId,
                                                             @PathVariable(value = "roomId") @Positive @Valid Long roomId) {
        return Response.success(roomService.deleteRoom(userId, roomId));
    }

    /**
     * 매물 단건 조회
     * @param roomId 조회할 매물의 ID
     * @return Success Response. RoomSearchResponse - 조회한 매물 정보 JSON DTO
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<Response<RoomSearchResponse>> getRoom(@PathVariable(name = "roomId") @Positive @Valid Long roomId) {
        return Response.success(roomService.getRoom(roomId));
    }

    // 매물 이미지 업로드

}
