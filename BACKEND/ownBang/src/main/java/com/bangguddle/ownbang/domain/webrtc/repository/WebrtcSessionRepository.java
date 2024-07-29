package com.bangguddle.ownbang.domain.webrtc.repository;

import com.bangguddle.ownbang.domain.webrtc.entity.WebrtcSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebrtcSessionRepository extends JpaRepository<WebrtcSession, Long> {

    // 예약 ID로 session 조회
    Optional<WebrtcSession> findByReservationId(Long reservationId);
}
