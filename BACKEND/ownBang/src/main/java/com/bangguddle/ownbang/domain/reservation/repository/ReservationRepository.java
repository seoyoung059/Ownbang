package com.bangguddle.ownbang.domain.reservation.repository;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND r.reservationTime = :reservationTime AND r.status = com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus.CONFIRMED")
    Optional<Reservation> findByRoomIdAndTimeWithLock(
            @Param("roomId") Long roomId,
            @Param("reservationTime") LocalDateTime reservationTime
    );

    // 예약 취소가 아닌 것 중에 중복건이 있나 확인
    Optional<Reservation> findByRoomIdAndUserIdAndStatusNot(Long roomId, Long userId, ReservationStatus status);

    List<Reservation> findByUserId(long userId);

    Optional<Reservation> findById(Long id);

    @Query("SELECT r FROM Reservation r WHERE r.room.agent.id = :agentId AND r.reservationTime >= :today ORDER BY r.reservationTime ASC, r.id ASC")
    List<Reservation> findByRoomAgentIdAndReservationTimeAfterOrderByReservationTimeAscIdAsc(
            @Param("agentId") Long agentId,
            @Param("today") LocalDateTime today
    );
}
