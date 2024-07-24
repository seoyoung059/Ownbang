package com.bangguddle.ownbang.domain.room.controller;

import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.service.RoomService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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

    // 매물 삭제

    // 매물 단건 조회

    // 매물 이미지 업로드

}
