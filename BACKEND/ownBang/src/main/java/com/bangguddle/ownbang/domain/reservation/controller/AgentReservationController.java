package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("agents/reservations")
@RequiredArgsConstructor // 의존성 주입: 생성자 주입을 임의의 코드없이 자동으로 설정
public class AgentReservationController {
    private final ReservationService reservationService;

    // 예약 확정
    @PatchMapping("/{id}")
    public ResponseEntity<Response<NoneResponse>> confirmStatusReservation(@AuthenticationPrincipal Long userId, @PathVariable(name="id") Long id) {
        SuccessResponse<NoneResponse> response = reservationService.confirmStatusReservation(userId, id);
        return Response.success(response);
    }
    // 예약 조회
    @GetMapping
    public ResponseEntity<Response<ReservationListResponse>> getAgentReservations(@AuthenticationPrincipal Long userId) {
        SuccessResponse<ReservationListResponse> response = reservationService.getAgentReservations(userId);
        return Response.success(response);
    }

    //예약 취소
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Response<NoneResponse>> deleteStatusReservation(@PathVariable(name="id") Long id, @AuthenticationPrincipal Long userId) {
        SuccessResponse<NoneResponse> response = reservationService.deleteStatusReservation(userId, id);
        return Response.success(response);
    }


}

