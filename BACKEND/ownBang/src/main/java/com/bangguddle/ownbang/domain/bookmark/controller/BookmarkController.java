package com.bangguddle.ownbang.domain.bookmark.controller;

import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkSearchResponse;
import com.bangguddle.ownbang.domain.bookmark.service.BookmarkService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{roomId}")
    public ResponseEntity<Response<NoneResponse>> toggleBookmark(@AuthenticationPrincipal Long userId,
                                                              @PathVariable(name = "roomId") @Positive @Valid Long roomId) {
        return Response.success(bookmarkService.toggleBookmark(userId, roomId));
    }

    @GetMapping
    public ResponseEntity<Response<List<BookmarkSearchResponse>>> getAllBookmarks(@AuthenticationPrincipal Long userId) {
        return Response.success(bookmarkService.getBookmarks(userId));
    }
}
