package com.bangguddle.ownbang.domain.room.service.impl;

import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.repository.RoomAppliancesRepository;
import com.bangguddle.ownbang.domain.room.repository.RoomDetailRepository;
import com.bangguddle.ownbang.domain.room.repository.RoomImageRepository;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.room.service.RoomService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomImageServiceImpl roomImageServiceImpl;
    private final RoomDetailRepository roomDetailRepository;
    private final RoomAppliancesRepository roomAppliancesRepository;
    private final RoomImageRepository roomImageRepository;
//    private final UserRepository userRepository;

    // 매물 생성
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> createRoom(RoomCreateRequest roomCreateRequest, List<MultipartFile> roomImageFiles) {
//        User agent = userRepository(findById(roomCreateRequest.getAgent())).orElseThrow(() -> new IllegalAccessException("Invalid Agent Id"));

        RoomDetail roomDetail = roomCreateRequest.roomDetailCreateRequest().toEntity();
        RoomAppliances roomAppliances = roomCreateRequest.roomAppliancesCreateRequest().toEntity();
        Room room = roomCreateRequest.toEntity(roomAppliances, roomDetail);

        for (MultipartFile roomImageFile : roomImageFiles) {
            roomImageServiceImpl.uploadImage(roomImageFile, room);
        }
        roomRepository.save(room);

        return new SuccessResponse<NoneResponse>(SuccessCode.ROOM_REGISTER_SUCCESS, NoneResponse.NONE);
    }


    // 매물 삭제
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> deleteRoom(Long roomId) {
//        User agent = userRepository(findById(roomCreateRequestDto.getAgent())).orElseThrow(() -> new IllegalAccessException("Invalid Agent Id"));
        roomRepository.validateById(roomId);
        roomRepository.deleteById(roomId);

        return new SuccessResponse<NoneResponse>(SuccessCode.ROOM_DELETE_SUCCESS, NoneResponse.NONE);
    }


}
