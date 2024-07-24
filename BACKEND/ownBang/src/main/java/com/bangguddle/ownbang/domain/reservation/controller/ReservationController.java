package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reservations")
@RequiredArgsConstructor // 의존성 주입: 생성자 주입을 임의의 코드없이 자동으로 설정

public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/")
    public ResponseEntity<Response<NoneResponse>> createReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        SuccessResponse<NoneResponse> response = reservationService.createReservation(reservationRequest);
        return Response.success(response);
    }

    @GetMapping("/list")
    public ResponseEntity<Response<ReservationListResponse>> getMyReservationList (@RequestParam(name="userId") Long userId) {
        SuccessResponse<ReservationListResponse> response = reservationService.getMyReservationList (userId);
        return Response.success(response);
    }
}


