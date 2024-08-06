package com.bangguddle.ownbang.domain.checklist.repository;

import com.bangguddle.ownbang.domain.checklist.entity.Checklist;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    Boolean existsByUserIdAndTitleAndIsTemplateTrue(Long userId, String title);
    Optional<Checklist> findByIdAndUserId(Long checklistId, Long userId);
    Optional<Checklist> findByIdAndUserIdAndIsTemplateTrue(Long reservationId, Long userId);
    List<Checklist> findByUserIdAndIsTemplateTrue(Long userId);
    Optional<Checklist> findByUserAndReservation(User user, Reservation reservation);
}
