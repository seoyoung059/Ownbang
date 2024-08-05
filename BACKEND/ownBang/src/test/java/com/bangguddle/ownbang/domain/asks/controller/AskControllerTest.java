package com.bangguddle.ownbang.domain.asks.controller;

import com.bangguddle.ownbang.domain.asks.dto.AskContentCreateRequest;
import com.bangguddle.ownbang.domain.asks.service.AskService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static com.bangguddle.ownbang.global.enums.SuccessCode.ASK_CONTENT_CREATE_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.ASK_CREATE_SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AskController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})
class AskControllerTest {

    static Long roomId, askId, userId;
    static String content;

    @MockBean
    private AskService askService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userId = 1L;
        roomId = 1L;
        askId = 1L;
        content = "test content!";
    }

    @Test
    @WithMockUser
    @DisplayName("Ask 생성 - 성공")
    void createAsk_Success() throws Exception {

        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ASK_CREATE_SUCCESS, NoneResponse.NONE);
        when(askService.createAsk(any(), anyLong())).thenReturn(success);

        mockMvc.perform(
                post("/asks/{roomId}", roomId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(ASK_CREATE_SUCCESS.name()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

    @Test
    @WithMockUser
    @DisplayName("Ask 생성 - 실패: 유효하지 않은 roomId")
    void createAsk_Fail_InvalidRoomId() throws Exception {
        roomId = -1L;

        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ASK_CREATE_SUCCESS, NoneResponse.NONE);
        when(askService.createAsk(anyLong(), anyLong())).thenReturn(success);

        mockMvc.perform(
                        post("/asks/{roomId}", roomId)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("AskContent 생성 - 성공")
    void createAskContent_Success() throws Exception {
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ASK_CONTENT_CREATE_SUCCESS, NoneResponse.NONE);
        AskContentCreateRequest request = AskContentCreateRequest.builder().askId(askId).content(content).build();

        when(askService.createAskContent(any(), any(AskContentCreateRequest.class))).thenReturn(success);

        mockMvc.perform(
                post("/asks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("Ask 생성 - 실패: 유효하지 않은 Ask ID")
    void createAsk_Fail_InvalidAskId() throws Exception {
        askId = -1L;

        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ASK_CONTENT_CREATE_SUCCESS, NoneResponse.NONE);
        AskContentCreateRequest request = AskContentCreateRequest.builder().askId(askId).content(content).build();

        when(askService.createAskContent(any(), any(AskContentCreateRequest.class))).thenReturn(success);

        mockMvc.perform(
                        post("/asks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Ask 생성 - 실패: 컨텐츠 blank")
    void createAsk_Fail_BlankContent() throws Exception {
        content = " ";

        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ASK_CONTENT_CREATE_SUCCESS, NoneResponse.NONE);
        AskContentCreateRequest request = AskContentCreateRequest.builder().askId(askId).content(content).build();

        when(askService.createAskContent(any(), any(AskContentCreateRequest.class))).thenReturn(success);

        mockMvc.perform(
                        post("/asks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());
    }
}