package com.bangguddle.ownbang.domain.reservation.dto;

import java.util.List;

public record UserReservationListResponse (
    List<UserReservationResponse> userReservations
){
    public static UserReservationListResponse from(List<UserReservationResponse> userReservations ) {
        return new UserReservationListResponse(userReservations);
    }
}