package com.bangguddle.ownbang.domain.bookmark.service.impl;

import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkSearchResponse;
import com.bangguddle.ownbang.domain.bookmark.entity.Bookmark;
import com.bangguddle.ownbang.domain.bookmark.repository.BookmarkRepository;
import com.bangguddle.ownbang.domain.bookmark.service.BookmarkService;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.BAD_REQUEST;
import static com.bangguddle.ownbang.global.enums.ErrorCode.BOOKMARK_DUPLICATED;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;


@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Override
    public SuccessResponse<NoneResponse> createBookmark(Long userId, Long roomId) {
        Room room = roomRepository.getById(roomId);
        User user = userRepository.getById(userId);
        validateBookmark(room, user);
        bookmarkRepository.save(Bookmark.builder().room(room).user(user).build());
        return new SuccessResponse<>(BOOKMARK_CREATE_SUCCESS, NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<NoneResponse> deleteBookmark(Long userId, Long bookmarkId) {
        userRepository.getById(userId);
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new AppException(BAD_REQUEST));
        if(!bookmark.getUser().getId().equals(userId)) throw new AppException(BAD_REQUEST);
        bookmarkRepository.deleteById(bookmarkId);
        return new SuccessResponse<>(BOOKMARK_DELETE_SUCCESS, NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<List<BookmarkSearchResponse>> getBookmarks(Long userId) {
        validateUser(userId);
        List<BookmarkSearchResponse> bookmarkList = bookmarkRepository.findByUserId(userId).stream()
                .map(BookmarkSearchResponse::from)
                .toList();
        return new SuccessResponse<>(BOOKMARK_FIND_SUCCESS, bookmarkList);
    }

    private void validateBookmark(Room room, User user) {
        if(bookmarkRepository.existsBookmarkByRoomIdAndUserId(room.getId(), user.getId())) {
            throw new AppException(BOOKMARK_DUPLICATED);
        }
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(BAD_REQUEST));
    }

    private Room validateRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(BAD_REQUEST));
    }

}
