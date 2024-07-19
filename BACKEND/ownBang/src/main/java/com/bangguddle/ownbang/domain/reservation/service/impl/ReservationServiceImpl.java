package com.bangguddle.ownbang.domain.reservation.service.impl;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationDto;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    @Override
    @Transactional
    public MessageResponse createReservation(ReservationDto reservationDto) {
        Long roomId = reservationDto.roomId();
        Long userId = reservationDto.userId();
        LocalDateTime time = reservationDto.time();

        // 룸 ID와 시간이 일치하는 예약이 이미 존재하는지 확인
        Optional<Reservation> existingReservation = reservationRepository.findByRoomIdAndTimeWithLock(roomId, time);

        if (existingReservation.isPresent()) {
            throw new AppException(ErrorCode.Reservation_DUPLICATED); // 이미 예약이 존재하는 경우
        }

        // 이미 내가 예약한 매물이라면 ,
        Optional<Reservation> completedReservation = reservationRepository.findByRoomIdAndUserId(roomId, userId);
        if (completedReservation.isPresent()) {
            throw new AppException(ErrorCode.Reservation_COMPLETED); // 이미 예약이 존재하는 경우
        }

        // 새 예약 저장
        Reservation reservation = reservationDto.toEntity();
        reservationRepository.save(reservation);

        return new MessageResponse(SuccessCode.RESERVATION_MAKE_SUCCESS.getMessage());
    }

    public ReservationListResponse getReservationsByUserId(long userId){
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(reservation -> new ReservationDto(
                        reservation.getId(),
                        reservation.getRoomId(),
                        reservation.getUserId(),
                        reservation.getTime(),
                        reservation.getStatus()
                ))
                .collect(Collectors.toList());

        return new ReservationListResponse(reservationDtos, "내가 신청한 예약 목록을 조회할 수 있습니다.");
    }
}
