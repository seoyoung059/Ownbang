package com.bangguddle.ownbang.domain.asks.service.impl;

import com.bangguddle.ownbang.domain.asks.dto.AskContentCreateRequest;
import com.bangguddle.ownbang.domain.asks.entity.Ask;
import com.bangguddle.ownbang.domain.asks.entity.AskContent;
import com.bangguddle.ownbang.domain.asks.repository.AskContentRepository;
import com.bangguddle.ownbang.domain.asks.repository.AskRepository;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.bangguddle.ownbang.global.enums.SuccessCode.ASK_CONTENT_CREATE_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.ASK_CREATE_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AskServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AskRepository askRepository;

    @Mock
    private AskContentRepository askContentRepository;

    @InjectMocks
    private AskServiceImpl askService;


    @Test
    @DisplayName("문의 생성 - 성공")
    void createAsk_Success() {
        Long userId = 1L, roomId = 1L;
        Room room = mock(Room.class);
        User user = mock(User.class);

        when(askRepository.existsByUserIdAndRoomId(anyLong(), anyLong())).thenReturn(false);
        when(roomRepository.getById(anyLong())).thenReturn(room);
        when(userRepository.getById(anyLong())).thenReturn(user);

        SuccessResponse<NoneResponse> response = askService.createAsk(userId, roomId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(ASK_CREATE_SUCCESS);

        verify(askRepository, times(1)).save(any(Ask.class));
    }


    @Test
    @DisplayName("문의 생성 - 실패: 이미 존재하는 문의")
    void createAsk_Fail_Duplicated() {
        Long userId = 1L, roomId = 1L;

        when(askRepository.existsByUserIdAndRoomId(anyLong(), anyLong())).thenReturn(true);

        assertThatThrownBy(() -> askService.createAsk(userId, roomId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.ASK_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("문의 생성 - 실패: 유효하지 않은 room id")
    void createAsk_Fail_InvalidRoomId() {
        Long userId = 1L, roomId = 1L;

        when(askRepository.existsByUserIdAndRoomId(anyLong(), anyLong())).thenReturn(false);
        doThrow(new AppException(ErrorCode.ROOM_NOT_FOUND)).when(roomRepository).getById(roomId);

        assertThatThrownBy(() -> askService.createAsk(userId, roomId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.ROOM_NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("문의 생성 - 실패: 유효하지 않은 user id")
    void createAsk_Fail_InvalidUserId() {
        Long userId = 1L, roomId = 1L;

        when(askRepository.existsByUserIdAndRoomId(anyLong(), anyLong())).thenReturn(false);
        when(roomRepository.getById(anyLong())).thenReturn(mock(Room.class));
        doThrow(new AppException(ErrorCode.USER_NOT_FOUND)).when(userRepository).getById(roomId);


        assertThatThrownBy(() -> askService.createAsk(userId, roomId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("임차인 문의 내용 생성 - 성공")
    void createAskContent_Success() {
        Long userId = 1L, askId = 1L;
        User user = mock(User.class);
        Ask ask = Ask.builder().user(user).build();
        AskContentCreateRequest request = AskContentCreateRequest.builder().askId(askId).build();

        doReturn(userId).when(user).getId();
        when(askRepository.getById(askId)).thenReturn(ask);
        when(userRepository.getById(userId)).thenReturn(user);

        SuccessResponse<NoneResponse> success = askService.createAskContent(userId,request);

        assertThat(success).isNotNull();
        assertThat(success.successCode()).isEqualTo(ASK_CONTENT_CREATE_SUCCESS);

        verify(askContentRepository, times(1)).save(any(AskContent.class));
    }


    @Test
    @DisplayName("임차인 문의 내용 생성 - 실패: 존재하지 않는 ask")
    void createAskContent_Fail_AskNotExist() {
        Long userId = 1L, askId = 1L;
        User user = mock(User.class);
        AskContentCreateRequest request = AskContentCreateRequest.builder().askId(askId).build();

        doThrow(new AppException(ErrorCode.ASK_NOT_FOUND)).when(userRepository).getById(userId);

        assertThatThrownBy(() -> askService.createAskContent(userId, request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.ASK_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("임차인 문의 내용 생성 - 실패: 유효하지 않은 user Id")
    void createAskContent_Fail_InvalidUser() {
        Long userId = 1L, askId = 1L;
        User user = mock(User.class);
        Ask ask = Ask.builder().user(user).build();
        AskContentCreateRequest request = AskContentCreateRequest.builder().askId(askId).build();

        when(askRepository.getById(askId)).thenReturn(ask);
        doThrow(new AppException(ErrorCode.USER_NOT_FOUND)).when(userRepository).getById(userId);

        assertThatThrownBy(() -> askService.createAskContent(userId, request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }




    @Test
    @DisplayName("임차인 문의 내용 생성 - 실패: ask에 해당하지 않는 user")
    void createAskContent_Fail_NotUserAsk() {
        Long userId = 1L, askUserId = 101L, askId = 1L;
        User user = mock(User.class);
        User askUser = mock(User.class);
        Ask ask = Ask.builder().user(askUser).build();
        AskContentCreateRequest request = AskContentCreateRequest.builder().askId(askId).build();

        doReturn(askUserId).when(askUser).getId();
        when(askRepository.getById(askId)).thenReturn(ask);
        when(userRepository.getById(userId)).thenReturn(user);


        assertThatThrownBy(() -> askService.createAskContent(userId, request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.ACCESS_DENIED.getMessage());
    }
}