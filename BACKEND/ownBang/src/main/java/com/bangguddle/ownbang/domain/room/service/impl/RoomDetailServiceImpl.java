package com.bangguddle.ownbang.domain.room.service.impl;

import com.bangguddle.ownbang.domain.room.repository.RoomDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomDetailServiceImpl {

    private final RoomDetailRepository roomDetailRepository;

}
