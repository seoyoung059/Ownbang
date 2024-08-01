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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    /**
     * 매물 생성
     * @param roomCreateRequest 매물 정보 JSON
     * @param roomImageFiles 매물 관련 이미지 파일
     * @return Success Response, 실패 시 AppException Throw
     */
    @PostMapping
    public ResponseEntity<Response<NoneResponse>> addRoom(@RequestPart(value = "roomCreateRequest") @Valid RoomCreateRequest roomCreateRequest,
                                                          @RequestPart(value = "roomImageFiles", required = false) List<MultipartFile> roomImageFiles) {
        return Response.success(roomService.createRoom(roomCreateRequest, roomImageFiles));
    }

    /**
     * 매물 정보 수정
     * @param roomId 수정할 매물의 ID
     * @param roomUpdateRequest 수정할 매물의 정보 JSON
     * @param roomImageFiles 매물 관련 새로 업로드할 이미지 파일
     * @return Success Response, 실패 시 AppException Throw
     */
    @PatchMapping("/{roomId}")
    public ResponseEntity<Response<NoneResponse>> updateRoom(@PathVariable(value = "roomId") @Valid @Positive Long roomId,
                                                             @RequestPart(value="roomUpdateRequest") @Valid RoomUpdateRequest roomUpdateRequest,
                                                             @RequestPart(value="roomImageFiles", required = false) List<MultipartFile> roomImageFiles) {
        return Response.success(roomService.updateRoom(roomId, roomUpdateRequest, roomImageFiles));
    }

    /**
     * 매물 삭제
     * @param roomId 삭제할 매물의 ID
     * @return Success Response, 실패 시 AppException Throw
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Response<NoneResponse>> deleteRoom(@PathVariable(value = "roomId") @Positive @Valid Long roomId) {
        return Response.success(roomService.deleteRoom(roomId));
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
