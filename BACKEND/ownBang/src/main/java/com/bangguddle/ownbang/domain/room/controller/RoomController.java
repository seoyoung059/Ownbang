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

    @PostMapping
    public ResponseEntity<Response<NoneResponse>> addRoom(@RequestPart(value = "roomCreateRequest") @Valid RoomCreateRequest roomCreateRequest,
                                                          @RequestPart(value = "roomImageFiles", required = false) List<MultipartFile> roomImageFiles) {
        return Response.success(roomService.createRoom(roomCreateRequest, roomImageFiles));
    }

    // 매물 수정
    @PatchMapping("/{roomId}")
    public ResponseEntity<Response<NoneResponse>> updateRoom(@PathVariable(value = "roomId") @Valid @Positive Long roomId,
                                                             @RequestPart(value="roomUpdateRequest") @Valid RoomUpdateRequest roomUpdateRequest,
                                                             @RequestPart(value="roomImageFiles", required = false) List<MultipartFile> roomImageFiles) {
        return Response.success(roomService.updateRoom(roomId, roomUpdateRequest, roomImageFiles));
    }

    // 매물 삭제
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Response<NoneResponse>> deleteRoom(@PathVariable(value = "roomId") @Positive @Valid Long roomId) {
        return Response.success(roomService.deleteRoom(roomId));
    }

    // 매물 단건 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<Response<RoomSearchResponse>> getRoom(@PathVariable(name = "roomId") @Positive @Valid Long roomId) {
        return Response.success(roomService.getRoom(roomId));
    }

    // 매물 이미지 업로드

}
