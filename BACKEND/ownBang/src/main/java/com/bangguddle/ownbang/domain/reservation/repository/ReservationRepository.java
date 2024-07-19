package com.bangguddle.ownbang.domain.reservation.repository;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락을 적용
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND r.time = :time And r.status<> 2") // 예약 취소가 아닌 것 중에 중복건이 있나 확인
    Optional<Reservation> findByRoomIdAndTimeWithLock(
            final Long roomId,
            final LocalDateTime time
    );


    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락을 적용
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND r.userId= :userId And r.status<> 2") // 예약 취소가 아닌 것 중에 중복건이 있나 확인
    Optional<Reservation> findByRoomIdAndUserId(final Long roomId, final long userId);

}
