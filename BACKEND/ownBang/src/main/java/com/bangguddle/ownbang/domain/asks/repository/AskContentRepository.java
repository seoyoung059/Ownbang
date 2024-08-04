package com.bangguddle.ownbang.domain.asks.repository;

import com.bangguddle.ownbang.domain.asks.entity.AskContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AskContentRepository extends JpaRepository<AskContent, Long> {
}
