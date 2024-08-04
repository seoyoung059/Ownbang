package com.bangguddle.ownbang.domain.video.repository;

import com.bangguddle.ownbang.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Long, Video> { }
