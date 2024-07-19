package com.bangguddle.ownbang.domain.reservation.dto;

import java.util.List;

public record ReservationListResponse (
        List<ReservationDto> reservations,
        String message
){

}
