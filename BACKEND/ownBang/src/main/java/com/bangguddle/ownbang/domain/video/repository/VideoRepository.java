package com.bangguddle.ownbang.domain.video.repository;

import com.bangguddle.ownbang.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByReservationId(Long reservationId);
}
