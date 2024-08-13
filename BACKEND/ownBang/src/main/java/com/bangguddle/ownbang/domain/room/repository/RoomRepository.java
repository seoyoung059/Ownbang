package com.bangguddle.ownbang.domain.room.repository;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.global.handler.AppException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.ROOM_NOT_FOUND;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> getByAgentId(Long agentId, Pageable pageable);
    List<Room> findAll();

    List<Room> findByGeoHashStartsWith(String geoHashPrefix);
  /**
     * 해당 ID의 매물이 존재하는지 확인
     * @param roomId 유효성 검사를 수행할 RoomId
     */
    default Room getById(final Long roomId) {
        return findById(roomId).orElseThrow(() -> new AppException(ROOM_NOT_FOUND));
    }

}
