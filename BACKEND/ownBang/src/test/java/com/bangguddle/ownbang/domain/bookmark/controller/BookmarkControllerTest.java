package com.bangguddle.ownbang.domain.bookmark.controller;

import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkSearchResponse;
import com.bangguddle.ownbang.domain.bookmark.service.BookmarkService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.ArrayList;
import java.util.List;

import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookmarkController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})
class BookmarkControllerTest {

    @MockBean
    private BookmarkService bookmarkService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("북마크 생성 - 성공")
    void addBookmark_Success() throws Exception {
        Long roomId = 1L;
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(BOOKMARK_CREATE_SUCCESS, NoneResponse.NONE);

        when(bookmarkService.createBookmark(any(), any())).thenReturn(success);

        mockMvc.perform(
                post("/bookmarks/{roomId}", roomId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(BOOKMARK_CREATE_SUCCESS.name()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

    @Test
    @WithMockUser
    @DisplayName("북마크 생성 - 실패: 유효하지 않은 값")
    void addBookmark_Failed_InvalidParam() throws Exception {
        Long roomId = -1L;
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(BOOKMARK_CREATE_SUCCESS, NoneResponse.NONE);

        when(bookmarkService.createBookmark(any(), anyLong())).thenReturn(success);

        mockMvc.perform(
                        post("/bookmarks/{roomId}", roomId)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("북마크 삭제 - 성공")
    void deleteBookmark_Success() throws Exception{
        Long bookmarkId = 1L;
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(BOOKMARK_DELETE_SUCCESS, NoneResponse.NONE);

        when(bookmarkService.deleteBookmark(any(), anyLong())).thenReturn(success);

        mockMvc.perform(
                delete("/bookmarks/{bookmarkId}", String.valueOf(bookmarkId))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(BOOKMARK_DELETE_SUCCESS.name()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

    @Test
    @WithMockUser
    @DisplayName("북마크 삭제 - 실패: 적절하지 않은 북마크 ID")
    void deleteBookmark_Fail_InvalidID() throws Exception{
        Long bookmarkId = -1L;
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(BOOKMARK_DELETE_SUCCESS, NoneResponse.NONE);

        when(bookmarkService.deleteBookmark(anyLong(), anyLong())).thenReturn(success);

        mockMvc.perform(
                        delete("/bookmarks/{bookmarkId}", String.valueOf(bookmarkId))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("유저 북마크 조회 - 성공")
    void getAllBookmarks_Success() throws Exception {
        Long userId = 1L;

        List<BookmarkSearchResponse> bookmarkList = new ArrayList<>();
        SuccessResponse<List<BookmarkSearchResponse>> success = new SuccessResponse<>(BOOKMARK_FIND_SUCCESS, bookmarkList);

        when(bookmarkService.getBookmarks(userId)).thenReturn(success);

        mockMvc.perform(
                get("/bookmarks")
                        .param("userId", String.valueOf(userId))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(BOOKMARK_FIND_SUCCESS.name()));
    }
}