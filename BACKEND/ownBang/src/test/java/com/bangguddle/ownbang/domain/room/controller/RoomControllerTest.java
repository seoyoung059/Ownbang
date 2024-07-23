package com.bangguddle.ownbang.domain.room.controller;

import com.bangguddle.ownbang.domain.room.dto.RoomAppliancesCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomCreateRequest;
import com.bangguddle.ownbang.domain.room.dto.RoomDetailCreateRequest;
import com.bangguddle.ownbang.domain.room.enums.*;
import com.bangguddle.ownbang.domain.room.service.impl.RoomServiceImpl;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;


@WebMvcTest(controllers = RoomController.class)
class RoomControllerTest {

    @MockBean
    private RoomServiceImpl roomServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("매물 Post 컨트롤러 확인")
    @WithMockUser
    public void createRoom() throws Exception {
        // DTO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(true,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) 1, (byte) 1,
                HeatingType.지역, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.남, Purpose.공동주택,
                "서울시 강남구 역삼대로", "멀티캠퍼스 역삼");
        RoomCreateRequest roomCreateRequest = RoomCreateRequest.of(DealType.월세, RoomType.오피스텔, Structure.분리형,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "url", roomAppliancesCreateRequest, roomDetailCreateRequest);

        MockMultipartFile file0 = new MockMultipartFile("roomCreateRequest", null, "application/json", objectMapper.writeValueAsString(roomCreateRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("roomImageFile", "image2.png", "image/png", "image/png".getBytes());
        SuccessResponse success = new SuccessResponse<>(SuccessCode.ROOM_REGISTER_SUCCESS, NoneResponse.NONE);

        // mock
        given(roomServiceImpl.createRoom(any(RoomCreateRequest.class), any())).willReturn(success);

        mockMvc.perform(
                        multipart("/api/rooms")
                                .file(file0)
                                .file(file1)
                                .file(file2)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(SuccessCode.ROOM_REGISTER_SUCCESS.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("NONE"));
    }



}
