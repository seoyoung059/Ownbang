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
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.ACCESS_DENIED;
import static com.bangguddle.ownbang.global.enums.ErrorCode.ROOM_NOT_FOUND;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomImageServiceImpl roomImageServiceImpl;
    private final UserRepository userRepository;

    /**
     * 매물 생성 Service 메서드
     * @param request 매물 생성 DTO
     * @param roomImageFiles 생성할 매물의 이미지 파일
     * @return SuccessResponse
     */
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> createRoom(Long userId, RoomCreateRequest request, List<MultipartFile> roomImageFiles) {
        User agent = userRepository.getById(userId);

        RoomDetail roomDetail = request.roomDetailCreateRequest().toEntity();
        RoomAppliances roomAppliances = request.roomAppliancesCreateRequest().toEntity();
        Room room = request.toEntity(agent, roomAppliances, roomDetail);

        for (MultipartFile roomImageFile : roomImageFiles) {
            roomImageServiceImpl.uploadImage(roomImageFile, room);
        }
        roomRepository.save(room);

        return new SuccessResponse<>(ROOM_CREATE_SUCCESS, NoneResponse.NONE);
    }

    /**
     * 매물 수정 서비스 메서드
     * @param roomId 수정할 매물의 ID
     * @param request 수정할 매물 정보
     * @param roomImageFiles 매물에 추가할 이미지 파일
     * @return Success Response.
     */
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> updateRoom(Long userId, Long roomId, RoomUpdateRequest request, List<MultipartFile> roomImageFiles) {
        System.out.println("roomId = " + roomId);
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ROOM_NOT_FOUND));
        System.out.println("ROOM EXISTS = " + existingRoom);
        validateAgent(userId, existingRoom);

        existingRoom.updateFromDto(request);

        // 삭제할 이미지 처리
        if(request.roomImageUpdateRequestList()!=null && !request.roomImageUpdateRequestList().isEmpty()) {
            for (RoomImageUpdateRequest imageRequest : request.roomImageUpdateRequestList()) {
                if (!imageRequest.isDeleted()) continue;
                roomImageServiceImpl.deleteImage(roomId, imageRequest.id());
            }
        }

        // 추가 이미지 업로드
        if(roomImageFiles!=null && !roomImageFiles.isEmpty()) {
            for (MultipartFile roomImageFile : roomImageFiles) {
                roomImageServiceImpl.uploadImage(roomImageFile, existingRoom);
            }
        }

        roomRepository.save(existingRoom);
        return new SuccessResponse<>(ROOM_UPDATE_SUCCESS, NoneResponse.NONE);
    }

    /**
     * 매물 삭제 서비스 메서드
     * @param roomId 삭제할 매물의 ID
     * @return Success Response
     */
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> deleteRoom(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(ROOM_NOT_FOUND));
        validateAgent(userId, room);
        roomRepository.deleteById(roomId);
        return new SuccessResponse<>(ROOM_DELETE_SUCCESS, NoneResponse.NONE);
    }


    /**
     * 매물 조회 서비스 메서드
     * @param roomId 조회할 매물의 ID
     * @return Success Response - RoomSearchResponse DTO
     */
    @Override
    @Transactional
    public SuccessResponse<RoomSearchResponse> getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(ROOM_NOT_FOUND));
        return new SuccessResponse<>(ROOM_FIND_SUCCESS, RoomSearchResponse.from(room));
    }

    private void validateAgent(Long userId, Room existingRoom) {
        System.out.println("ValidateAgent");
        User agent = userRepository.getById(userId);
        if(agent != existingRoom.getAgent())
            throw new AppException(ACCESS_DENIED);
        System.out.println("finfin");
    }
}
