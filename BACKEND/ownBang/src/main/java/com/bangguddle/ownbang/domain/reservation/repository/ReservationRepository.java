package com.bangguddle.ownbang.domain.reservation.repository;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 사용
    @Query("SELECT r FROM Reservation r WHERE r.id = :id")
    Optional<Reservation> findByIdWithLock(Long id);


}
