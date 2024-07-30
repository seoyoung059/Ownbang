package com.bangguddle.ownbang.domain.room.service.impl;

import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomListSearchResponse;
import com.bangguddle.ownbang.domain.room.dto.RoomSearchResponse;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.room.service.RoomService;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomImageServiceImpl roomImageServiceImpl;
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


    // 매물 조회
    @Override
    @Transactional
    public SuccessResponse<RoomSearchResponse> getRoom(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty()) {
            throw new AppException(ErrorCode.ROOM_NOT_FOUND);
        }

        return new SuccessResponse<>(SuccessCode.ROOM_FIND_SUCCESS, RoomSearchResponse.from(room.get()));
    }

    // 매물 전체 조회
    @Override
    @Transactional
    public SuccessResponse<List<RoomListSearchResponse>> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomListSearchResponse> roomSearchResponses = rooms.stream()
                .map(RoomListSearchResponse::from)
                .toList();

        return new SuccessResponse<>(SuccessCode.ROOM_FIND_SUCCESS, roomSearchResponses);
    }
}
