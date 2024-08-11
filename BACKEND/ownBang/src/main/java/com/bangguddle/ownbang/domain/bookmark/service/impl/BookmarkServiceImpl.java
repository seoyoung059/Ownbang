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
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.BAD_REQUEST;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;


@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Override
    public SuccessResponse<NoneResponse> toggleBookmark(Long userId, Long roomId) {
        Room room = roomRepository.getById(roomId);
        User user = userRepository.getById(userId);

        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByRoomIdAndUserId(roomId, userId);
        if(bookmark.isPresent()) {
            bookmarkRepository.delete(bookmark.get());
            return new SuccessResponse<>(BOOKMARK_DELETE_SUCCESS, NoneResponse.NONE);
        } else {
            bookmarkRepository.save(Bookmark.builder().room(room).user(user).build());
            return new SuccessResponse<>(BOOKMARK_CREATE_SUCCESS, NoneResponse.NONE);
        }

    }

    @Override
    public SuccessResponse<List<BookmarkSearchResponse>> getBookmarks(Long userId) {
        validateUser(userId);
        List<BookmarkSearchResponse> bookmarkList = bookmarkRepository.findByUserId(userId).stream()
                .map(BookmarkSearchResponse::from)
                .toList();
        return new SuccessResponse<>(BOOKMARK_FIND_SUCCESS, bookmarkList);
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
