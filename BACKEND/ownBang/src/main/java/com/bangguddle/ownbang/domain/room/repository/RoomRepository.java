package com.bangguddle.ownbang.domain.room.repository;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.handler.AppException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    default void validateById(final Long id) {
        if(!existsById(id))
            throw new AppException(ErrorCode.ROOM_NOT_FOUND);
    }
}
