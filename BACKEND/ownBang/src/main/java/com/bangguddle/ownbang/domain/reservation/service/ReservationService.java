//package com.bangguddle.ownbang.service;
//
//import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
//import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
//import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//public class ReservationService {
//
//
//    final ReservationRepository reservationRepository;
//
//    // 예약 신청 메서드
//    @Transactional
// 열거가 아니라 dto 매개변수로 수정하기
//    public Reservation applyReservation(Long roomId, Long userId, LocalDateTime time, ReservationStatus status) {
//        // 예약 생성
//        toEntity()
//
//        // 예약 저장
//        return reservationRepository.save(reservation);
//    }
//
//    // 예약 조회 메서드
//    public Optional<Reservation> findReservationById(Long id) {
//        return reservationRepository.findById(id);
//    }
//}
