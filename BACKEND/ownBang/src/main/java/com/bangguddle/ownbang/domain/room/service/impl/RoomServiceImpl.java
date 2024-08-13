package com.bangguddle.ownbang.domain.room.service.impl;

import ch.hsr.geohash.GeoHash;
import com.bangguddle.ownbang.domain.agent.dto.AgentResponse;
import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.bookmark.repository.BookmarkRepository;
import com.bangguddle.ownbang.domain.review.repository.ReviewRepository;
import com.bangguddle.ownbang.domain.room.dto.*;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.entity.RoomImage;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.room.service.RoomImageService;
import com.bangguddle.ownbang.domain.room.service.RoomService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final RoomImageService roomImageService;
    private final AgentRepository agentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 매물 생성 Service 메서드
     * @param request 매물 생성 DTO
     * @param roomImageFiles 생성할 매물의 이미지 파일
     * @return SuccessResponse
     */
    @Override
    @Transactional
    public SuccessResponse<NoneResponse> createRoom(Long userId, RoomCreateRequest request, List<MultipartFile> roomImageFiles) {
        Agent agent = agentRepository.getByUserId(userId);

        RoomDetail roomDetail = request.roomDetailCreateRequest().toEntity();
        RoomAppliances roomAppliances = request.roomAppliancesCreateRequest().toEntity();
        Room room = request.toEntity(agent, roomAppliances, roomDetail);

        if(roomImageFiles!=null && !roomImageFiles.isEmpty()) {
            for (MultipartFile roomImageFile : roomImageFiles) {
                roomImageService.uploadImageToS3(roomImageFile, room);
            }
        }
        setRoomImage(room);
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
    public SuccessResponse<NoneResponse> modifyRoom(Long userId, Long roomId, RoomUpdateRequest request, List<MultipartFile> roomImageFiles) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ROOM_NOT_FOUND));
        validateAgent(userId, existingRoom);

        existingRoom.updateFromDto(request);

        // 삭제할 이미지 처리
        List<RoomImageUpdateRequest> requestList = request.roomImageUpdateRequestList();
        List<RoomImage> imageList = existingRoom.getRoomImages();
        if(requestList!=null && !requestList.isEmpty()) {
            int length = request.roomImageUpdateRequestList().size();
            int j = 0;
            for (int i = 0; i < length; i++) {
                if(imageList.size() <= j) break;
                if (!requestList.get(i).isDeleted()) {
                    j++;
                    continue;
                }
                imageList.remove(j);
            }
        }

        // 추가 이미지 업로드
        if(roomImageFiles!=null && !roomImageFiles.isEmpty()) {
            for (MultipartFile roomImageFile : roomImageFiles) {
                roomImageService.uploadImageToS3(roomImageFile, existingRoom);
            }
        }
        setRoomImage(existingRoom);
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
    public SuccessResponse<RoomSearchResponse> getRoom(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(ROOM_NOT_FOUND));
        boolean isBookmarked = false;
        if(userId!=null){
            isBookmarked = bookmarkRepository.findBookmarkByRoomIdAndUserId(room.getId(), userId).isPresent();
        }
        Agent agent = room.getAgent();
        AgentResponse agentResponse = AgentResponse.from(agent, reviewRepository.calculateAverageStarRatingByAgentId(agent.getId()));
        return new SuccessResponse<>(ROOM_FIND_SUCCESS, RoomSearchResponse.from(room, agentResponse, isBookmarked));
    }

    /**
     * 특정 중개인이 올린 Room 검색 메서드
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public SuccessResponse<List<RoomInfoSearchResponse>> getAgentRooms(Long userId, int page, int size) {
        Agent agent = agentRepository.getByUserId(userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<RoomInfoSearchResponse> list = roomRepository.getByAgentId(agent.getId(), pageable).stream()
                .map((room)->{
                    return RoomInfoSearchResponse.from(room, false);
                })
                .toList();
        return new SuccessResponse<>(ROOM_FIND_SUCCESS, list);
    }

    @Override
    public SuccessResponse<List<RoomInfoSearchResponse>> search(Long userId, Float lat, Float lon) {
        String geoHashPrefix = GeoHash.geoHashStringWithCharacterPrecision(lat, lon, 3);
        List<Room> rooms = roomRepository.findByGeoHashStartsWith(geoHashPrefix);
        List<RoomInfoSearchResponse> response = rooms.stream()
                .map(room -> {
                    boolean isBookmarked = userId != null && bookmarkRepository
                            .findBookmarkByRoomIdAndUserId(room.getId(), userId).isPresent();
                    return RoomInfoSearchResponse.from(room, isBookmarked);
                })
                .toList();
        return new SuccessResponse<>(SEARCH_ROOM_SUCCESS, response);

    }

    private void validateAgent(Long userId, Room existingRoom) {
        Agent agent = agentRepository.getByUserId(userId);
        if(agent != existingRoom.getAgent())
            throw new AppException(ACCESS_DENIED);
    }

    private void setRoomImage(Room room){
        if(!room.getRoomImages().isEmpty()) room.updateProfileImage(room.getRoomImages().get(0).getRoomImageUrl());
    }
}
