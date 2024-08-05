package com.bangguddle.ownbang.domain.bookmark.service;

import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkSearchResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

import java.util.List;

public interface BookmarkService {
    SuccessResponse<NoneResponse> createBookmark(Long userId, Long roomId);

    SuccessResponse<NoneResponse> deleteBookmark(Long userId, Long bookmarkId);

    SuccessResponse<List<BookmarkSearchResponse>> getBookmarks(Long userId);
}
