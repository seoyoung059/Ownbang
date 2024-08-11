package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.*;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("reservations")
@RequiredArgsConstructor // 의존성 주입: 생성자 주입을 임의의 코드없이 자동으로 설정

public class ReservationController {
    private final ReservationService reservationService;

    /**
     * 예약 생성
     *
     * @param reservationRequest 예약 정보 JSON
     * @return Success Response, 실패 시 AppException Throw
     */
    @PostMapping("/")
    public ResponseEntity<Response<NoneResponse>> createReservation(@AuthenticationPrincipal Long userId,
                                                                    @Valid @RequestBody ReservationRequest reservationRequest) {
        SuccessResponse<NoneResponse> response = reservationService.createReservation(userId, reservationRequest);
        return Response.success(response);
    }

    /**
     * 임차인 예약 목록 조회
     *
     * @return Success Response, UserReservationListResponse 실패 시 AppException Throw
     */
    @GetMapping("/list")
    public ResponseEntity<Response<UserReservationListResponse>> getMyReservationList (@AuthenticationPrincipal Long userId) {
        SuccessResponse<UserReservationListResponse> response = reservationService.getMyReservationList (userId);
        return Response.success(response);
    }

    /**
     * 임차인 예약 철회
     *
     * @param id :철회할 예약 id
     * @return Success Response, 실패 시 AppException Throw
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Response<NoneResponse>> updateStatusReservation(@PathVariable(name="id") Long id, @AuthenticationPrincipal Long userId) {
        SuccessResponse<NoneResponse> response = reservationService.updateStatusReservation(userId, id);
        return Response.success(response);
    }

    /**
     * 예약 가능한 시간 조회
     *
     * @param roomId 조회하고 싶은 매물 ID
     * @param date  조회를 하고 싶은 날짜
     * @return SuccessResponse, AvailableTimeResponse 실패 시 AppException Throw
     */
    @GetMapping("/available-times")
    public ResponseEntity<Response<AvailableTimeResponse>> getAvailableTimes(
            @RequestParam("roomId") Long roomId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        AvailableTimeRequest request = new AvailableTimeRequest(roomId, date);
        SuccessResponse<AvailableTimeResponse> response = reservationService.getAvailableTimes(request);
        return Response.success(response);
    }

}


