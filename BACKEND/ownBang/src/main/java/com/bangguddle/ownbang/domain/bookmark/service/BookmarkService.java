package com.bangguddle.ownbang.domain.bookmark.service;

import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkSearchResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

import java.util.List;

public interface BookmarkService {
    SuccessResponse<NoneResponse> toggleBookmark(Long userId, Long roomId);

    SuccessResponse<List<BookmarkSearchResponse>> getBookmarks(Long userId);
}
