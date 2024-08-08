package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.AvailableTimeRequest;
import com.bangguddle.ownbang.domain.reservation.dto.AvailableTimeResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
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

    @PostMapping("/")
    public ResponseEntity<Response<NoneResponse>> createReservation(@AuthenticationPrincipal Long userId,
                                                                    @Valid @RequestBody ReservationRequest reservationRequest) {
        SuccessResponse<NoneResponse> response = reservationService.createReservation(userId, reservationRequest);
        return Response.success(response);
    }

    @GetMapping("/list")
    public ResponseEntity<Response<ReservationListResponse>> getMyReservationList (@AuthenticationPrincipal Long userId) {
        SuccessResponse<ReservationListResponse> response = reservationService.getMyReservationList (userId);
        return Response.success(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response<NoneResponse>> updateStatusReservation(@PathVariable(name="id") Long id, @AuthenticationPrincipal Long userId) {
        SuccessResponse<NoneResponse> response = reservationService.updateStatusReservation(userId, id);
        return Response.success(response);
    }

    @GetMapping("/available-times")
    public ResponseEntity<Response<AvailableTimeResponse>> getAvailableTimes(
            @RequestParam("roomId") Long roomId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        AvailableTimeRequest request = new AvailableTimeRequest(roomId, date);
        SuccessResponse<AvailableTimeResponse> response = reservationService.getAvailableTimes(request);
        return Response.success(response);
    }


}


