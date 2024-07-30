package com.bangguddle.ownbang.domain.search.repository;

import com.bangguddle.ownbang.domain.search.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long> {
}