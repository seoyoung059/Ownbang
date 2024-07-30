package com.bangguddle.ownbang.domain.room.service.impl;

import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomImageUpdateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomSearchResponse;
import com.bangguddle.ownbang.domain.room.dto.RoomUpdateRequest;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.room.service.RoomService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.ROOM_NOT_FOUND;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomImageServiceImpl roomImageServiceImpl;
//    private final UserRepository userRepository;

    /**
     * 매물 생성 Service 메서드
     * @param roomCreateRequest 매물 생성 DTO
     * @param roomImageFiles 생성할 매물의 이미지 파일
     * @return SuccessResponse
     */
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

        return new SuccessResponse<NoneResponse>(ROOM_CREATE_SUCCESS, NoneResponse.NONE);
    }

    /**
     * 매물 수정 서비스 메서드
     * @param roomId 수정할 매물의 ID
     * @param roomUpdateRequest 수정할 매물 정보
     * @param roomImageFiles 매물에 추가할 이미지 파일
     * @return Success Response.
     */
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> updateRoom(Long roomId, RoomUpdateRequest roomUpdateRequest, List<MultipartFile> roomImageFiles) {
        // User

        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ROOM_NOT_FOUND));

        existingRoom.updateFromDto(roomUpdateRequest);

        // 삭제할 이미지 처리
        if(roomUpdateRequest.roomImageUpdateRequestList()!=null && !roomUpdateRequest.roomImageUpdateRequestList().isEmpty()) {
            for (RoomImageUpdateRequest roomImageUpdateRequest : roomUpdateRequest.roomImageUpdateRequestList()) {
                if (!roomImageUpdateRequest.isDeleted()) continue;
                roomImageServiceImpl.deleteImage(roomImageUpdateRequest.id());
            }
        }

        // 추가 이미지 업로드
        if(roomImageFiles!=null && !roomImageFiles.isEmpty()) {
            for (MultipartFile roomImageFile : roomImageFiles) {
                roomImageServiceImpl.uploadImage(roomImageFile, existingRoom);
            }
        }

        roomRepository.save(existingRoom);
        return new SuccessResponse<NoneResponse>(ROOM_UPDATE_SUCCESS, NoneResponse.NONE);
    }

    /**
     * 매물 삭제 서비스 메서드
     * @param roomId 삭제할 매물의 ID
     * @return Success Response
     */
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> deleteRoom(Long roomId) {
//        User agent = userRepository(findById(roomCreateRequestDto.getAgent())).orElseThrow(() -> new IllegalAccessException("Invalid Agent Id"));
        roomRepository.validateById(roomId);
        roomRepository.deleteById(roomId);

        return new SuccessResponse<NoneResponse>(ROOM_DELETE_SUCCESS, NoneResponse.NONE);
    }


    /**
     * 매물 조회 서비스 메서드
     * @param roomId 조회할 매물의 ID
     * @return Success Response - RoomSearchResponse DTO
     */
    @Override
    @Transactional
    public SuccessResponse<RoomSearchResponse> getRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isEmpty()) {
            throw new AppException(ROOM_NOT_FOUND);
        }

        return new SuccessResponse<>(ROOM_FIND_SUCCESS, RoomSearchResponse.from(room.get()));
    }
}
