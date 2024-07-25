package com.bangguddle.ownbang.domain.room.service.impl;

import com.bangguddle.ownbang.domain.room.dto.RoomAppliancesCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomDetailCreateRequest;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.enums.*;
import com.bangguddle.ownbang.domain.room.repository.RoomAppliancesRepository;
import com.bangguddle.ownbang.domain.room.repository.RoomDetailRepository;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomDetailRepository roomDetailRepository;

    @Mock
    private RoomAppliancesRepository roomAppliancesRepository;

    @Mock
    private RoomImageServiceImpl roomImageServiceImpl;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    @Test
    @DisplayName("새 매물 생성 - 성공")
    void createRoomTest_SUCCESS() throws ParseException {
        //given
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(true,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) 1, (byte) 1,
                HeatingType.지역, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.남, Purpose.공동주택,
                "서울시 강남구 역삼대로", "멀티캠퍼스 역삼");
        RoomCreateRequest roomCreateRequest = RoomCreateRequest.of(DealType.월세, RoomType.오피스텔, Structure.분리형,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "url", roomAppliancesCreateRequest, roomDetailCreateRequest);

        List<MultipartFile> roomImageFiles = new ArrayList<>();
        roomImageFiles.add(new MockMultipartFile("file", "image1.png", "image/png", "image/png".getBytes()));
        roomImageFiles.add(new MockMultipartFile("file", "image2.png", "image/png", "image/png".getBytes()));

        // mock
        when(roomImageServiceImpl.uploadImage(any(MultipartFile.class), any(Room.class))).thenReturn(new SuccessResponse<>(SuccessCode.ROOM_REGISTER_SUCCESS, NoneResponse.NONE));
        when(roomRepository.save(any(Room.class))).thenReturn(Room.builder().build()); // 추가된 부분


        // when
        SuccessResponse<NoneResponse> response = roomServiceImpl.createRoom(roomCreateRequest, roomImageFiles);

        // then
        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(SuccessCode.ROOM_REGISTER_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(roomRepository, times(1)).save(any(Room.class));
        verify(roomImageServiceImpl, times(roomImageFiles.size())).uploadImage(any(MultipartFile.class), any(Room.class));

    }


    @Test
    @DisplayName("이미지 업로드 실패 시 서버 내부 에러 발생")
    void createRoomTest_ImageFailed() throws IOException, ParseException {
        //given
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(true,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) 1, (byte) 1,
                HeatingType.지역, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.남, Purpose.공동주택,
                "서울시 강남구 역삼대로", "멀티캠퍼스 역삼");
        RoomCreateRequest roomCreateRequest = RoomCreateRequest.of(DealType.월세, RoomType.오피스텔, Structure.분리형,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "url", roomAppliancesCreateRequest, roomDetailCreateRequest);
        List<MultipartFile> roomImageFiles = new ArrayList<>();
        roomImageFiles.add(new MockMultipartFile("file", "image1.png", "image/png", "image/png".getBytes()));
        roomImageFiles.add(new MockMultipartFile("file", "image2.png", "image/png", "image/png".getBytes()));

        // when
        when(roomImageServiceImpl.uploadImage(any(MultipartFile.class), any(Room.class))).thenThrow(new AppException(ErrorCode.INTERNAL_SERVER_ERROR));

        // 매물 생성
        assertThatThrownBy(()->{
            roomServiceImpl.createRoom(roomCreateRequest, roomImageFiles);
        }).isInstanceOf(AppException.class);


        verify(roomRepository, never()).save(any(Room.class));

    }


    @Test
    @DisplayName("매물 삭제 성공")
    void deleteRoomTest_Success() throws ParseException {
        //given
        Long roomId = 1L;

        //when
        SuccessResponse<NoneResponse> response = roomServiceImpl.deleteRoom(roomId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(SuccessCode.ROOM_DELETE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(roomRepository, times(1)).deleteById(roomId);
    }

    @Test
    @DisplayName("매물 삭제 실패 - 존재하지 않는 ID")
    void deleteRoomTest_Fail_NotExist() {
        //given
        doThrow(new AppException(ErrorCode.ROOM_NOT_FOUND)).when(roomRepository).validateById(anyLong());
        Long roomId = 1L;

        //when, then
        assertThatThrownBy(() -> {
            roomServiceImpl.deleteRoom(roomId);
        }).isInstanceOf(AppException.class);
    }

    @Test
    @DisplayName("매물 조회 성공")
    void findRoomTest_Success() {
        // given
        Room room = Room.builder().build();
        when(roomRepository.findById(anyLong())).thenReturn(Optional.ofNullable(room));
        Long roomId = 1L;

        //when
        roomServiceImpl.getRoom(roomId);

        //then
        verify(roomRepository, times(1)).findById(anyLong());
    }
}