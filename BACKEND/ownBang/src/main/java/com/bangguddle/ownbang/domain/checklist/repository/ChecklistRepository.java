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
    Boolean existsChecklistByUserIdAndTitleAndIsTemplate(Long userId, String title, Boolean isTemplate);
    Optional<Checklist> findChecklistByIdAndUserId(Long checklistId, Long userId);
    Optional<Checklist> findChecklistByIdAndIsTemplate(Long userId, Boolean isTemplate);
    List<Checklist> findByUserIdAndIsTemplateTrue(Long userId);
    Optional<Checklist> findByUserAndReservation(User user, Reservation reservation);
}
