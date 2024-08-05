package com.bangguddle.ownbang.domain.checklist.repository;

import com.bangguddle.ownbang.domain.checklist.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    Boolean existsChecklistByUserIdAndTitleAndIsTemplate(Long userId, String title, Boolean isTemplate);
    Optional<Checklist> findChecklistByIdAndUserId(Long checklistId, Long userId);
    Optional<Checklist> findChecklistByIdAndIsTemplate(Long userId, Boolean isTemplate);
}
