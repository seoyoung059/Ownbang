package com.bangguddle.ownbang.domain.reservation.service.impl;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public SuccessResponse<NoneResponse> createReservation(ReservationRequest reservationRequest) {
        Long roomId = reservationRequest.roomId();
        Long userId = reservationRequest.userId();
        LocalDateTime reservationTime = reservationRequest.reservationTime();

        // 룸 ID와 시간이 일치하는 예약이 이미 존재하는지 확인
        Optional<Reservation> existingReservation = reservationRepository.findByRoomIdAndTimeWithLock(roomId, reservationTime);

        if (existingReservation.isPresent()) {
            throw new AppException(RESERVATION_DUPLICATED); // 이미 예약이 존재하는 경우
        }

        // 이미 내가 예약한 매물이라면,
        Optional<Reservation> completedReservation = reservationRepository.findByRoomIdAndUserIdAndStatusNot(roomId, userId, ReservationStatus.CANCELED);
        if (completedReservation.isPresent()) {
            throw new AppException(RESERVATION_COMPLETED); // 이미 예약이 존재하는 경우
        }

        // 새 예약 저장
        Reservation reservation = reservationRequest.toEntity();
        reservationRepository.save(reservation);

        return new SuccessResponse<>(RESERVATION_MAKE_SUCCESS, NoneResponse.NONE);
    }

    public SuccessResponse<ReservationListResponse> getMyReservationList(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        if (reservations == null || reservations.isEmpty()) {
            // 예약이 없는 경우에 대한 처리
            return new SuccessResponse<>(RESERVATION_LIST_EMPTY, new ReservationListResponse(List.of()));
        }

        ReservationListResponse reservationListResponse = new ReservationListResponse(reservations);
        return new SuccessResponse<>(RESERVATION_LIST_SUCCESS, reservationListResponse);
    }

    // 예약 철회 시 사용
    public SuccessResponse<NoneResponse> updateStatusReservation(Long id) {
        Reservation reservation = vaildateReservation(id);

        // 이미 취소된 예약인지 확인
        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new AppException(RESERVATION_CANCELED_DUPLICATED);
        }

        // 이미 확정된 예약인지 확인
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new AppException(RESERVATION_CANCELED_UNAVAILABLE);
        }

        // 상태를 '예약취소'로 변경
        Reservation updatedReservation = reservation.withStatus();

        // 상태 변경된 예약 저장
        reservationRepository.save(updatedReservation);

        return new SuccessResponse<>(RESERVATION_UPDATE_STATUS_SUCCESS, NoneResponse.NONE);
    }

    private Reservation vaildateReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new AppException(BAD_REQUEST));
    }
}
