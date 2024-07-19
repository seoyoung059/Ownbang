package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationDto;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.global.response.MessageResponse;
import com.bangguddle.ownbang.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reservations")
@RequiredArgsConstructor // 의존성 주입: 생성자 주입을 임의의 코드없이 자동으로 설정

public class ReservationController {
    private final ReservationService reservationService;
    @PostMapping("/make")
    public ResponseEntity<Response<MessageResponse>> createReservation(@RequestBody ReservationDto reservationdto) {
        MessageResponse response = reservationService.createReservation(reservationdto);
        return Response.created(response);
    }

}
