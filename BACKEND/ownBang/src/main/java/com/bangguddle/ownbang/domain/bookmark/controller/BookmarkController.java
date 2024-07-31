package com.bangguddle.ownbang.domain.bookmark.controller;

import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkCreateRequest;
import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkSearchResponse;
import com.bangguddle.ownbang.domain.bookmark.service.BookmarkService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Response<NoneResponse>> addBookmark(@RequestBody @Valid BookmarkCreateRequest bookmarkCreateRequest) {
        return Response.success(bookmarkService.createBookmark(bookmarkCreateRequest));
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Response<NoneResponse>> deleteBookmark(@PathVariable(name = "bookmarkId") @Positive @Valid Long bookmarkId) {
        return Response.success(bookmarkService.deleteBookmark(bookmarkId));
    }

    @GetMapping
    public ResponseEntity<Response<List<BookmarkSearchResponse>>> getAllBookmarks(@RequestParam @Positive @Valid Long userId) {
        return Response.success(bookmarkService.getBookmarks(userId));
    }
}
