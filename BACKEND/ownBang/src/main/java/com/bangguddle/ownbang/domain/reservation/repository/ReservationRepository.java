package com.bangguddle.ownbang.domain.reservation.repository;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{


    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락을 적용
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND r.reservationTime = :reservationTime And r.status<> 'CANCELED'") // 예약 취소가 아닌 것 중에 중복건이 있나 확인
    Optional<Reservation> findByRoomIdAndTimeWithLock(
            final Long roomId,
            final LocalDateTime reservationTime
    );

    // 예약 취소가 아닌 것 중에 중복건이 있나 확인
    Optional<Reservation> findByRoomIdAndUserIdAndStatusNot(Long roomId, Long userId, ReservationStatus status);

    List<Reservation> findByUserId(long userId);

    Optional<Reservation> findById(Long id);
}
