package com.bangguddle.ownbang.domain.reservation.service.impl;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> createReservation(ReservationRequest reservationRequest) {
        Long roomId = reservationRequest.roomId();
        Long userId = reservationRequest.userId();
        LocalDateTime time = reservationRequest.time();

        // 룸 ID와 시간이 일치하는 예약이 이미 존재하는지 확인
        Optional<Reservation> existingReservation = reservationRepository.findByRoomIdAndTimeWithLock(roomId, time);

        if (existingReservation.isPresent()) {
            throw new AppException(ErrorCode.RESERVATION_DUPLICATED); // 이미 예약이 존재하는 경우
        }

        // 이미 내가 예약한 매물이라면,
        Optional<Reservation> completedReservation = reservationRepository.findByRoomIdAndUserIdAndStatusNot(roomId, userId, ReservationStatus.예약취소);
        if (completedReservation.isPresent()) {
            throw new AppException(ErrorCode.RESERVATION_COMPLETED); // 이미 예약이 존재하는 경우
        }

        // 새 예약 저장
        Reservation reservation = reservationRequest.toEntity();
        reservationRepository.save(reservation);

        return new SuccessResponse<>(SuccessCode.RESERVATION_MAKE_SUCCESS, NoneResponse.NONE);
    }


    public SuccessResponse<ReservationListResponse> getMyReservationList (Long userId){
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        if (reservations == null || reservations.isEmpty()) {
            // 예약이 없는 경우에 대한 처리
            return new SuccessResponse<>(SuccessCode.RESERVATION_LIST_EMPTY, new ReservationListResponse(List.of()));
        }

        ReservationListResponse reservationListResponse= new ReservationListResponse(reservations);
        return new SuccessResponse<>(SuccessCode.RESERVATION_LIST_SUCCESS,reservationListResponse );
    }

    public SuccessResponse<NoneResponse> deleteReservation(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservationRepository.deleteById(id);
            return new SuccessResponse<>(SuccessCode.RESERVATION_DELETE_SUCCESS, NoneResponse.NONE);
        } else {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
    }

}
