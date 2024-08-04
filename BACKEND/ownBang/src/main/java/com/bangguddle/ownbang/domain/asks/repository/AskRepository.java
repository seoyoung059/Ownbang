package com.bangguddle.ownbang.domain.asks.repository;

import com.bangguddle.ownbang.domain.asks.entity.Ask;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.handler.AppException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AskRepository extends JpaRepository<Ask, Long> {

    boolean existsByUserIdAndRoomId(Long userId, Long roomId);

    default Ask getById(Long id) {
        return findById(id).orElseThrow(()->new AppException(ErrorCode.ASK_NOT_FOUND));
    }
}
