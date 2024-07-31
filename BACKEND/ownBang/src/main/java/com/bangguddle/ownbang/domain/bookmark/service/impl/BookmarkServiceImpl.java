package com.bangguddle.ownbang.domain.bookmark.service.impl;

import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkCreateRequest;
import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkSearchResponse;
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
    public SuccessResponse<NoneResponse> createBookmark(BookmarkCreateRequest bookmarkCreateRequest) {
        Room room = roomRepository.findById(bookmarkCreateRequest.roomId())
                .orElseThrow(() -> new AppException(BAD_REQUEST));
        User user = userRepository.findById(bookmarkCreateRequest.userId())
                        .orElseThrow(()-> new AppException(BAD_REQUEST));
        if(bookmarkRepository.existsBookmarkByRoomIdAndUserId(room.getId(), user.getId())) {
            throw new AppException(BOOKMARK_DUPLICATED);
        }
        bookmarkRepository.save(bookmarkCreateRequest.toEntity(user, room));
        return new SuccessResponse<>(BOOKMARK_CREATE_SUCCESS, NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<NoneResponse> deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
        return new SuccessResponse<>(BOOKMARK_DELETE_SUCCESS, NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<List<BookmarkSearchResponse>> getBookmarks(Long userId) {
        List<BookmarkSearchResponse> bookmarkList = bookmarkRepository.findByUserId(userId).stream()
                .map(BookmarkSearchResponse::from)
                .toList();
        return new SuccessResponse<>(BOOKMARK_FIND_SUCCESS, bookmarkList);
    }
}
