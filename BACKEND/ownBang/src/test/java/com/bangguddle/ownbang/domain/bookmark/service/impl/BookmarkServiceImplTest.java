package com.bangguddle.ownbang.domain.bookmark.service.impl;

import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkCreateRequest;
import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkSearchResponse;
import com.bangguddle.ownbang.domain.bookmark.entity.Bookmark;
import com.bangguddle.ownbang.domain.bookmark.repository.BookmarkRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceImplTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookmarkServiceImpl bookmarkService;

    @Test
    @DisplayName("북마크 생성 - 성공")
    void createBookmark_Success() {
        // given
        Long roomId = 1L, userId = 1L;
        BookmarkCreateRequest bookmarkCreateRequest = BookmarkCreateRequest.of(roomId, userId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.ofNullable(Room.builder().build()));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(User.builder().build()));

        SuccessResponse<NoneResponse> response = bookmarkService.createBookmark(bookmarkCreateRequest);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(BOOKMARK_CREATE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));

    }

    @Test
    @DisplayName("북마크 생성 - 실패: 유효하지 않은 매물 번호")
    void createBookmark_Failed_NoRoomId() {
        // given
        Long roomId = 1L, userId = 1L;
        BookmarkCreateRequest bookmarkCreateRequest = BookmarkCreateRequest.of(roomId, userId);

        doThrow(new AppException(ErrorCode.BAD_REQUEST)).when(roomRepository).findById(roomId);

        assertThatThrownBy(() -> {
            bookmarkService.createBookmark(bookmarkCreateRequest);
        })
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.BAD_REQUEST.getMessage());
    }

    @Test
    @DisplayName("북마크 생성 - 실패: 유효하지 않은 유저 ID")
    void createBookmark_Failed_NoUserId() {
        // given
        Long roomId = 1L, userId = 1L;
        BookmarkCreateRequest bookmarkCreateRequest = BookmarkCreateRequest.of(roomId, userId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.ofNullable(Room.builder().build()));
        doThrow(new AppException(ErrorCode.BAD_REQUEST)).when(userRepository).findById(roomId);

        assertThatThrownBy(() ->
            bookmarkService.createBookmark(bookmarkCreateRequest)
        )
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.BAD_REQUEST.getMessage());
    }

    @Test
    @DisplayName("북마크 삭제 - 성공")
    void deleteBookmark_Success() {
        Long bookmarkId = 1L;

        SuccessResponse<NoneResponse> response = bookmarkService.deleteBookmark(bookmarkId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(BOOKMARK_DELETE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(bookmarkRepository, times(1)).deleteById(bookmarkId);
    }


    @Test
    @DisplayName("매물 삭제 - 실패: 존재하지 않는 ID")
    void deleteBookmarkTest_Fail_NotExits() {
        //given
        Long bookmarkId = 1L;
        doThrow(new AppException(ErrorCode.BAD_REQUEST)).when(bookmarkRepository).deleteById(bookmarkId);

        //when, then
        assertThatThrownBy(() ->
            bookmarkService.deleteBookmark(bookmarkId)
        ).isInstanceOf(AppException.class);
    }

    @Test
    @DisplayName("유저 북마크 조회 - 성공")
    void getBookmarks_Success() {
        Long userId = 1L;
        User user = User.builder().build();
        Room room1 = Room.builder().build();
        Room room2 = Room.builder().build();
        List<Bookmark> bookmarkList = new ArrayList<>();
        bookmarkList.add(Bookmark.builder().user(user).room(room1).build());
        bookmarkList.add(Bookmark.builder().user(user).room(room2).build());

        when(bookmarkRepository.findByUserId(userId)).thenReturn(bookmarkList);

        SuccessResponse<List<BookmarkSearchResponse>> response = bookmarkService.getBookmarks(userId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(BOOKMARK_FIND_SUCCESS);
        assertThat(response.data()).size().isEqualTo(bookmarkList.size());

        verify(bookmarkRepository, times(1)).findByUserId(userId);
    }

}