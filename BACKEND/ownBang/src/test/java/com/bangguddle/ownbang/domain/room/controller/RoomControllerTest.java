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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(controllers = RoomController.class)
class RoomControllerTest {

    @MockBean
    private RoomServiceImpl roomServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("매물 Post 컨트롤러 성공")
    @WithMockUser
    public void createRoom_Success() throws Exception {
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

        // when
        mockMvc.perform(
                        multipart("/api/rooms")
                                .file(file0)
                                .file(file1)
                                .file(file2)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )

                //then
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(SuccessCode.ROOM_REGISTER_SUCCESS.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("NONE"));
    }


    @Test
    @WithMockUser
    @DisplayName("Invalid Field 검증 - 조건 불만족")
    public void createRoom_Fail_InvalidField() throws Exception {
        // DTO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(true,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) -1, (byte) 1,
                HeatingType.지역, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.남, Purpose.공동주택,
                "서울시 강남구 역삼대로", "멀티캠퍼스 역삼");
        RoomCreateRequest invalidRequest = RoomCreateRequest.of(DealType.월세, RoomType.오피스텔, Structure.분리형,
                true, 12.88f, 15.66f, (byte) 1, -999999999L, 10L, 10L,
                "parcel", "url", roomAppliancesCreateRequest, roomDetailCreateRequest);
        MockMultipartFile file0 = new MockMultipartFile("roomCreateRequest", null, "application/json", objectMapper.writeValueAsString(invalidRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("roomImageFile", "image2.png", "image/png", "image/png".getBytes());

        // when
        mockMvc.perform(
                        multipart("/api/rooms")
                                .file(file0)
                                .file(file1)
                                .file(file2)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser
    @DisplayName("Invalid Field 검증 - 데이터가 없을 때")
    public void createRoom_Fail_NoData() throws Exception {
        // DTO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(null,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) 1, (byte) 1,
                HeatingType.지역, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.남, Purpose.공동주택,
                "서울시 강남구 역삼대로", "멀티캠퍼스 역삼");
        RoomCreateRequest invalidRequest = RoomCreateRequest.of(DealType.월세, RoomType.오피스텔, Structure.분리형,
                true, 12.88f, 15.66f, (byte) 1, 999999999L, 10L, 10L,
                "parcel", "url", roomAppliancesCreateRequest, roomDetailCreateRequest);
        MockMultipartFile file0 = new MockMultipartFile("roomCreateRequest", null, "application/json", objectMapper.writeValueAsString(invalidRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("roomImageFile", "image2.png", "image/png", "image/png".getBytes());

        // when
        mockMvc.perform(
                        multipart("/api/rooms")
                                .file(file1)
                                .file(file2)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @WithMockUser
    @DisplayName("Invalid Field 검증 - 필드가 비어 있을 때 - 아직 valid 설정 안함")
    public void createRoom_Fail_NullField() throws Exception {
        // DTO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(null,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) 1, (byte) 1,
                HeatingType.지역, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.남, Purpose.공동주택,
                "서울시 강남구 역삼대로", "멀티캠퍼스 역삼");
        RoomCreateRequest invalidRequest = RoomCreateRequest.of(DealType.월세, RoomType.오피스텔, Structure.분리형,
                true, 12.88f, 15.66f, (byte) 1, 999999999L, 10L, 10L,
                "parcel", "url", roomAppliancesCreateRequest, roomDetailCreateRequest);
        MockMultipartFile file0 = new MockMultipartFile("roomCreateRequest", null, "application/json", objectMapper.writeValueAsString(invalidRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("roomImageFile", "image2.png", "image/png", "image/png".getBytes());

        // when
        mockMvc.perform(
                        multipart("/api/rooms")
                                .file(file0)
                                .file(file1)
                                .file(file2)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @WithMockUser
    @DisplayName("객체 삭제 검증")
    public void deleteRoom_Success() throws Exception {
        // DTO
        Long roomId = 1L;
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(SuccessCode.ROOM_DELETE_SUCCESS, NoneResponse.NONE);

        // when
        when(roomServiceImpl.deleteRoom(anyLong())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                    delete("/api/rooms")
                        .param("roomId", String.valueOf(roomId))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()
                        ))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("객체 삭제 실패 - 적절하지 않은 매물 번호")
    public void deleteRoom_Fail_InappropriateId() throws Exception {
        // DTO
        Long roomId = -1L;
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(SuccessCode.ROOM_DELETE_SUCCESS, NoneResponse.NONE);

        // when
        when(roomServiceImpl.deleteRoom(anyLong())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                        delete("/api/rooms")
                                .param("roomId", String.valueOf(roomId))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()
                                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @WithMockUser
    @DisplayName("객체 삭제 실패 - Id 누락")
    public void deleteRoom_Fail_NoId() throws Exception {
        // DTO
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(SuccessCode.ROOM_DELETE_SUCCESS, NoneResponse.NONE);

        // when
        when(roomServiceImpl.deleteRoom(anyLong())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                        delete("/api/rooms")
                                .with(SecurityMockMvcRequestPostProcessors.csrf()
                                ))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
