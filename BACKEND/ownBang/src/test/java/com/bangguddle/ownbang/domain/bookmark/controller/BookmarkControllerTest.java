package com.bangguddle.ownbang.domain.bookmark.controller;

import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkCreateRequest;
import com.bangguddle.ownbang.domain.bookmark.dto.BookmarkSearchResponse;
import com.bangguddle.ownbang.domain.bookmark.service.BookmarkService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookmarkController.class)
class BookmarkControllerTest {

    @MockBean
    private BookmarkService bookmarkService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("북마크 생성 - 성공")
    void addBookmark_Success() throws Exception {
        Long roomId = 1L, userId = 1L;
        BookmarkCreateRequest bookmarkCreateRequest = BookmarkCreateRequest.of(roomId, userId);
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(BOOKMARK_CREATE_SUCCESS, NoneResponse.NONE);
        ObjectMapper objectMapper = new ObjectMapper();

        when(bookmarkService.createBookmark(bookmarkCreateRequest)).thenReturn(success);

        mockMvc.perform(
                post("/api/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookmarkCreateRequest))
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
        Long roomId = -1L, userId = 1L;
        BookmarkCreateRequest bookmarkCreateRequest = BookmarkCreateRequest.of(roomId, userId);
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(BOOKMARK_CREATE_SUCCESS, NoneResponse.NONE);
        ObjectMapper objectMapper = new ObjectMapper();

        when(bookmarkService.createBookmark(bookmarkCreateRequest)).thenReturn(success);

        mockMvc.perform(
                        post("/api/bookmarks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookmarkCreateRequest))
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

        when(bookmarkService.deleteBookmark(anyLong())).thenReturn(success);

        mockMvc.perform(
                delete("/api/bookmarks/{bookmarkId}", String.valueOf(bookmarkId))
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

        when(bookmarkService.deleteBookmark(anyLong())).thenReturn(success);

        mockMvc.perform(
                        delete("/api/bookmarks/{bookmarkId}", String.valueOf(bookmarkId))
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
                get("/api/bookmarks")
                        .param("userId", String.valueOf(userId))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(BOOKMARK_FIND_SUCCESS.name()));
    }
}