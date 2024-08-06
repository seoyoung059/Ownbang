package com.bangguddle.ownbang.domain.room.service.impl;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.room.dto.*;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.*;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.bangguddle.ownbang.global.enums.ErrorCode.ROOM_NOT_FOUND;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;


    @Mock
    private AgentRepository agentRepository;

    @Mock
    private RoomImageServiceImpl roomImageServiceImpl;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    @Test
    @DisplayName("매물 생성 - 성공")
    void createRoomTest_SUCCESS() throws ParseException {
        //given
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        User agent = User.builder().build();
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(true,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);
        RoomCreateRequest roomCreateRequest = RoomCreateRequest.of(37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesCreateRequest, roomDetailCreateRequest);

        List<MultipartFile> roomImageFiles = new ArrayList<>();
        roomImageFiles.add(new MockMultipartFile("file", "image1.png", "image/png", "image/png".getBytes()));
        roomImageFiles.add(new MockMultipartFile("file", "image2.png", "image/png", "image/png".getBytes()));

        // mock
        when(roomImageServiceImpl.uploadImageToS3(any(MultipartFile.class), any(Room.class))).thenReturn(new SuccessResponse<>(ROOM_IMAGE_UPLOAD_SUCCESS, NoneResponse.NONE));
        when(roomRepository.save(any(Room.class))).thenReturn(Room.builder().build()); // 추가된 부분


        // when
        SuccessResponse<NoneResponse> response = roomServiceImpl.createRoom(1L, roomCreateRequest, roomImageFiles);

        // then
        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(ROOM_CREATE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(roomRepository, times(1)).save(any(Room.class));
        verify(roomImageServiceImpl, times(roomImageFiles.size())).uploadImageToS3(any(MultipartFile.class), any(Room.class));

    }


    @Test
    @DisplayName("매물 생성 - 실패: 이미지 업로드 실패")
    void createRoomTest_ImageFailed() throws ParseException {
        //given
        Long userId = 1L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(true,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);
        RoomCreateRequest roomCreateRequest = RoomCreateRequest.of(37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesCreateRequest, roomDetailCreateRequest);
        List<MultipartFile> roomImageFiles = new ArrayList<>();
        roomImageFiles.add(new MockMultipartFile("file", "image1.png", "image/png", "image/png".getBytes()));
        roomImageFiles.add(new MockMultipartFile("file", "image2.png", "image/png", "image/png".getBytes()));

        // when
        when(roomImageServiceImpl.uploadImageToS3(any(MultipartFile.class), any(Room.class))).thenThrow(new AppException(INTERNAL_SERVER_ERROR));

        // 매물 생성
        assertThatThrownBy(()->
            roomServiceImpl.createRoom(userId, roomCreateRequest, roomImageFiles)
        ).isInstanceOf(AppException.class);


        verify(roomRepository, never()).save(any(Room.class));

    }


    @Test
    @DisplayName("매물 삭제 - 성공")
    void deleteRoomTest_Success() {
        //given
        Long userId = 1L, roomId = 1L;

        //mock
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(Room.builder().build()));

        //when
        SuccessResponse<NoneResponse> response = roomServiceImpl.deleteRoom(userId, roomId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(ROOM_DELETE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(roomRepository, times(1)).deleteById(roomId);
    }

    @Test
    @DisplayName("매물 삭제 - 실패: 존재하지 않는 ID")
    void deleteRoomTest_Fail_NotExist() {
        //given
        doThrow(new AppException(ROOM_NOT_FOUND)).when(roomRepository).findById(anyLong());
        Long userId = 1L, roomId = 1L;

        //when, then
        assertThatThrownBy(() ->
            roomServiceImpl.deleteRoom(userId, roomId)
        ).isInstanceOf(AppException.class);
    }

    @Test
    @DisplayName("매물 단건 조회 - 성공")
    void findRoomTest_Success() {
        // given
        Room room = Room.builder()
                .roomDetail(RoomDetail.builder().build())
                .roomAppliances(RoomAppliances.builder().build())
                .build();
        when(roomRepository.findById(anyLong())).thenReturn(Optional.ofNullable(room));
        Long roomId = 1L;

        //when
        roomServiceImpl.getRoom(roomId);

        //then
        verify(roomRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("매물 단건 조회 - 실패: 존재하지 않는 Id")
    void findRoomTest_Fail_InvalidId() {
        // given
        doThrow(new AppException(ROOM_NOT_FOUND)).when(roomRepository).findById(anyLong());
        Long roomId = 1L;

        //when

        //then
        assertThatThrownBy(() ->
            roomServiceImpl.getRoom(roomId)
        ).isInstanceOf(AppException.class);
    }


    @Test
    @DisplayName("매물 수정 - 성공")
    void modifyRoomTest_SUCCESS() throws ParseException {
        // DTO
        Long userId = 1L, roomId = 1L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest = RoomAppliancesUpdateRequest.of(1L, true,
                true, true, true, true, true, true, true);
        RoomDetailUpdateRequest roomDetailUpdateRequest = RoomDetailUpdateRequest.of(1L, (byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);

        RoomImageUpdateRequest roomImageUpdateRequest1 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", true);
        RoomImageUpdateRequest roomImageUpdateRequest2 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        RoomImageUpdateRequest roomImageUpdateRequest3 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        List<RoomImageUpdateRequest> roomImageUpdateRequestList = new ArrayList<>();
        roomImageUpdateRequestList.add(roomImageUpdateRequest1);
        roomImageUpdateRequestList.add(roomImageUpdateRequest2);
        roomImageUpdateRequestList.add(roomImageUpdateRequest3);

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.of(roomId, 37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesUpdateRequest, roomDetailUpdateRequest, roomImageUpdateRequestList);

        List<MultipartFile> roomImageFiles = new ArrayList<>();
        roomImageFiles.add(new MockMultipartFile("file", "image1.png", "image/png", "image/png".getBytes()));
        roomImageFiles.add(new MockMultipartFile("file", "image2.png", "image/png", "image/png".getBytes()));

        Room room = Room.builder()
                .roomAppliances(RoomAppliances.builder().build())
                .roomDetail(RoomDetail.builder().build())
                .build();


        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ROOM_UPDATE_SUCCESS, NoneResponse.NONE);

        // mock
        when(roomRepository.findById(anyLong())).thenReturn(Optional.ofNullable(room));
        when(roomImageServiceImpl.uploadImageToS3(any(MultipartFile.class), any(Room.class))).thenReturn(new SuccessResponse<>(ROOM_IMAGE_UPLOAD_SUCCESS, NoneResponse.NONE));
        when(roomImageServiceImpl.deleteImage(anyLong(), anyLong())).thenReturn(success);
        when(roomRepository.save(any(Room.class))).thenReturn(any(Room.class)); // 추가된 부분


        // when
        SuccessResponse<NoneResponse> response = roomServiceImpl.modifyRoom(userId, roomId, roomUpdateRequest, roomImageFiles);

        // then
        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(ROOM_UPDATE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(roomRepository, times(1)).save(any(Room.class));
        verify(roomImageServiceImpl, times(roomImageFiles.size())).uploadImageToS3(any(MultipartFile.class), any(Room.class));
        verify(roomRepository, times(1)).findById(anyLong());

    }

    @Test
    @DisplayName("매물 수정 - 실패: 존재하지 않는 매물")
    void modifyRoomTest_RoomIdNotExist() throws ParseException {
        // DTO
        Long userId = 1L, roomId = 1L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest = RoomAppliancesUpdateRequest.of(1L, true,
                true, true, true, true, true, true, true);
        RoomDetailUpdateRequest roomDetailUpdateRequest = RoomDetailUpdateRequest.of(1L, (byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);

        RoomImageUpdateRequest roomImageUpdateRequest1 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", true);
        RoomImageUpdateRequest roomImageUpdateRequest2 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        RoomImageUpdateRequest roomImageUpdateRequest3 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        List<RoomImageUpdateRequest> roomImageUpdateRequestList = new ArrayList<>();
        roomImageUpdateRequestList.add(roomImageUpdateRequest1);
        roomImageUpdateRequestList.add(roomImageUpdateRequest2);
        roomImageUpdateRequestList.add(roomImageUpdateRequest3);

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.of(roomId, 37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesUpdateRequest, roomDetailUpdateRequest, roomImageUpdateRequestList);

        List<MultipartFile> roomImageFiles = new ArrayList<>();
        roomImageFiles.add(new MockMultipartFile("file", "image1.png", "image/png", "image/png".getBytes()));
        roomImageFiles.add(new MockMultipartFile("file", "image2.png", "image/png", "image/png".getBytes()));

        // mock
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        // 매물 생성
        assertThatThrownBy(()->
            roomServiceImpl.modifyRoom(userId, roomId, roomUpdateRequest, roomImageFiles)
        ).isInstanceOf(AppException.class);


        verify(roomRepository, never()).save(any(Room.class));

    }

    @Test
    @DisplayName("매물 수정 - 실패: 이미지 업로드 실패")
    void modifyRoomTest_ImageUploadFailed() throws ParseException {
        // DTO
        Long userId = 1L, roomId = 1L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest = RoomAppliancesUpdateRequest.of(1L, true,
                true, true, true, true, true, true, true);
        RoomDetailUpdateRequest roomDetailUpdateRequest = RoomDetailUpdateRequest.of(1L, (byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);

        RoomImageUpdateRequest roomImageUpdateRequest1 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", true);
        RoomImageUpdateRequest roomImageUpdateRequest2 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        RoomImageUpdateRequest roomImageUpdateRequest3 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        List<RoomImageUpdateRequest> roomImageUpdateRequestList = new ArrayList<>();
        roomImageUpdateRequestList.add(roomImageUpdateRequest1);
        roomImageUpdateRequestList.add(roomImageUpdateRequest2);
        roomImageUpdateRequestList.add(roomImageUpdateRequest3);

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.of(roomId, 37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesUpdateRequest, roomDetailUpdateRequest, roomImageUpdateRequestList);

        List<MultipartFile> roomImageFiles = new ArrayList<>();
        roomImageFiles.add(new MockMultipartFile("file", "image1.png", "image/png", "image/png".getBytes()));
        roomImageFiles.add(new MockMultipartFile("file", "image2.png", "image/png", "image/png".getBytes()));

        Room room = Room.builder()
                .roomAppliances(RoomAppliances.builder().build())
                .roomDetail(RoomDetail.builder().build())
                .build();


        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ROOM_UPDATE_SUCCESS, NoneResponse.NONE);

        // mock
        when(roomRepository.findById(anyLong())).thenReturn(Optional.ofNullable(room));
        when(roomImageServiceImpl.uploadImageToS3(any(MultipartFile.class), any(Room.class))).thenThrow(new AppException(INTERNAL_SERVER_ERROR));
        when(roomImageServiceImpl.deleteImage(anyLong(), anyLong())).thenReturn(success);

        // 매물 생성
        assertThatThrownBy(()->
            roomServiceImpl.modifyRoom(userId, roomId, roomUpdateRequest, roomImageFiles)
        ).isInstanceOf(AppException.class);


        verify(roomRepository, never()).save(any(Room.class));

    }

    @Test
    @DisplayName("매물 수정 - 실패: 이미지 삭제 실패")
    void modifyRoomTest_ImageDeleteFailed() throws ParseException {
        // DTO
        Long userId = 1L, roomId = 1L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest = RoomAppliancesUpdateRequest.of(1L, true,
                true, true, true, true, true, true, true);
        RoomDetailUpdateRequest roomDetailUpdateRequest = RoomDetailUpdateRequest.of(1L, (byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);

        RoomImageUpdateRequest roomImageUpdateRequest1 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", true);
        RoomImageUpdateRequest roomImageUpdateRequest2 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        RoomImageUpdateRequest roomImageUpdateRequest3 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        List<RoomImageUpdateRequest> roomImageUpdateRequestList = new ArrayList<>();
        roomImageUpdateRequestList.add(roomImageUpdateRequest1);
        roomImageUpdateRequestList.add(roomImageUpdateRequest2);
        roomImageUpdateRequestList.add(roomImageUpdateRequest3);

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.of(roomId, 37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesUpdateRequest, roomDetailUpdateRequest, roomImageUpdateRequestList);

        List<MultipartFile> roomImageFiles = new ArrayList<>();
        roomImageFiles.add(new MockMultipartFile("file", "image1.png", "image/png", "image/png".getBytes()));
        roomImageFiles.add(new MockMultipartFile("file", "image2.png", "image/png", "image/png".getBytes()));

        Room room = Room.builder()
                .roomAppliances(RoomAppliances.builder().build())
                .roomDetail(RoomDetail.builder().build())
                .build();

        // mock
        when(roomRepository.findById(anyLong())).thenReturn(Optional.ofNullable(room));
        when(roomImageServiceImpl.deleteImage(anyLong(), anyLong())).thenThrow(new AppException(INTERNAL_SERVER_ERROR));

        // 매물 생성
        assertThatThrownBy(()->
            roomServiceImpl.modifyRoom(userId, roomId, roomUpdateRequest, roomImageFiles)
        ).isInstanceOf(AppException.class);


        verify(roomRepository, never()).save(any(Room.class));

    }

    @Test
    @DisplayName("중개인 매물 목록 조회 - 성공")
    void getAgentRooms_Success() {
        Long userId = 1L, agentId = 1L; int page = 0, size = 10, dataSize = 8;
        Agent agent = mock(Agent.class);
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < dataSize; i++) {
            rooms.add(Room.builder().agent(agent).build());
        }

        when(agent.getId()).thenReturn(agentId);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);
        when(roomRepository.getByAgentId(anyLong(), any(Pageable.class))).thenReturn(rooms);


        SuccessResponse<List<RoomInfoSearchResponse>> response = roomServiceImpl.getAgentRooms(userId, page, size);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(ROOM_FIND_SUCCESS);
        assertThat(response.data()).size().isEqualTo(dataSize);

        verify(roomRepository, times(1)).getByAgentId(anyLong(), any(Pageable.class));
    }

}