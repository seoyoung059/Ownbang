package com.bangguddle.ownbang.domain.room.repository;

import com.bangguddle.ownbang.domain.room.entity.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
}
