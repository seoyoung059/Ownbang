package com.bangguddle.ownbang.domain.room.controller;

import com.bangguddle.ownbang.domain.room.dto.*;
import com.bangguddle.ownbang.domain.room.enums.*;
import com.bangguddle.ownbang.domain.room.service.impl.RoomServiceImpl;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.BAD_REQUEST;
import static com.bangguddle.ownbang.global.enums.ErrorCode.METHOD_NOT_ALLOWED;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RoomController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})
class RoomControllerTest {

    @MockBean
    private RoomServiceImpl roomServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("매물 생성 - 성공")
    @WithMockUser
    public void createRoom_Success() throws Exception {
        // DTO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(true,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);
        RoomCreateRequest roomCreateRequest = RoomCreateRequest.of(37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel","서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesCreateRequest, roomDetailCreateRequest);

        MockMultipartFile file0 = new MockMultipartFile("roomCreateRequest", null, "application/json", objectMapper.writeValueAsString(roomCreateRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("roomImageFile", "image2.png", "image/png", "image/png".getBytes());
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ROOM_CREATE_SUCCESS, NoneResponse.NONE);

        // mock
        given(roomServiceImpl.createRoom(any(), any(RoomCreateRequest.class), any())).willReturn(success);

        // when
        mockMvc.perform(
                        multipart("/rooms/agents")
                                .file(file0)
                                .file(file1)
                                .file(file2)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )

                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(ROOM_CREATE_SUCCESS.name()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }


    @Test
    @WithMockUser
    @DisplayName("매물 생성 - 실패: Invalid Field - 조건 불만족")
    public void createRoom_Fail_InvalidField() throws Exception {
        // DTO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(true,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) -1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);
        RoomCreateRequest invalidRequest = RoomCreateRequest.of(37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, -999999999L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesCreateRequest, roomDetailCreateRequest);
        MockMultipartFile file0 = new MockMultipartFile("roomCreateRequest", null, "application/json", objectMapper.writeValueAsString(invalidRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("roomImageFile", "image2.png", "image/png", "image/png".getBytes());

        // when
        mockMvc.perform(
                        multipart("/rooms/agents")
                                .file(file0)
                                .file(file1)
                                .file(file2)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("ERROR"));

    }

    @Test
    @WithMockUser
    @DisplayName("매물 생성 - 실패: Invalid Field 검증 - 필드 누락")
    public void createRoom_Fail_NoData() throws Exception {
        // DTO
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("roomImageFile", "image2.png", "image/png", "image/png".getBytes());

        // when
        mockMvc.perform(
                        multipart("/rooms/agents")
                                .file(file1)
                                .file(file2)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    @DisplayName("매물 생성 - 실패: Invalid Field 검증 - null 필드")
    public void createRoom_Fail_NullField() throws Exception {
        // DTO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesCreateRequest roomAppliancesCreateRequest = RoomAppliancesCreateRequest.of(null,
                true, true, true, true, true, true, true);
        RoomDetailCreateRequest roomDetailCreateRequest = RoomDetailCreateRequest.of((byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);
        RoomCreateRequest invalidRequest = RoomCreateRequest.of(37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 999999999L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesCreateRequest, roomDetailCreateRequest);
        MockMultipartFile file0 = new MockMultipartFile("roomCreateRequest", null, "application/json", objectMapper.writeValueAsString(invalidRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("roomImageFile", "image2.png", "image/png", "image/png".getBytes());

        // when
        mockMvc.perform(
                        multipart("/rooms/agents")
                                .file(file0)
                                .file(file1)
                                .file(file2)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("ERROR"));
    }

    @Test
    @WithMockUser
    @DisplayName("매물 삭제 - 성공")
    public void deleteRoom_Success() throws Exception {
        // DTO
        Long roomId = 1L;
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(ROOM_DELETE_SUCCESS, NoneResponse.NONE);

        // when
        when(roomServiceImpl.deleteRoom(any(), anyLong())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                        delete("/rooms/agents/{roomID}", String.valueOf(roomId))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value("ROOM_DELETE_SUCCESS"));
    }

    @Test
    @WithMockUser
    @DisplayName("매물 삭제 -  실패: 적절하지 않은 매물번호")
    public void deleteRoom_Fail_InappropriateId() throws Exception {
        // DTO
        Long roomId = -1L;
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(ROOM_DELETE_SUCCESS, NoneResponse.NONE);

        // when
        when(roomServiceImpl.deleteRoom(anyLong(), anyLong())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                        delete("/rooms/agents/{roomID}", String.valueOf(roomId))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()
                                ))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    @DisplayName("매물 삭제 - 실패: Id 누락")
    public void deleteRoom_Fail_NoId() throws Exception {
        // DTO
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(ROOM_DELETE_SUCCESS, NoneResponse.NONE);

        // when
        when(roomServiceImpl.deleteRoom(anyLong(), anyLong())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                        delete("/rooms/agents")
                                .with(SecurityMockMvcRequestPostProcessors.csrf()
                                ))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value(METHOD_NOT_ALLOWED.getMessage()));
    }

    @Test
    @WithMockUser
    @DisplayName("매물 단건 조회 - 성공")
    public void findRoom_Success() throws Exception {
        // DTO
        Long roomId = 1L;
        RoomSearchResponse roomSearchResponse = RoomSearchResponse.builder().build();
        SuccessResponse<RoomSearchResponse> successResponse = new SuccessResponse<>(ROOM_FIND_SUCCESS, roomSearchResponse);

        // when
        when(roomServiceImpl.getRoom(any(), anyLong())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                        get("/rooms/search/{roomId}", String.valueOf(roomId))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()
                                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ROOM_FIND_SUCCESS"));
    }

    @Test
    @WithMockUser
    @DisplayName("매물 단건 조회 - 실패: 적절하지 않은 id")
    public void findRoom_Fail_InvalidId() throws Exception {
        // DTO
        Long roomId = -1L;
        RoomSearchResponse roomSearchResponse = RoomSearchResponse.builder().build();
        SuccessResponse<RoomSearchResponse> successResponse = new SuccessResponse<>(ROOM_FIND_SUCCESS, roomSearchResponse);

        // when
        when(roomServiceImpl.getRoom(any(), anyLong())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                        get("/rooms/search/{roomId}", String.valueOf(roomId))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()
                                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST.getMessage()));
    }


    @Test
    @DisplayName("매물 수정 - 성공")
    @WithMockUser
    public void modifyRoom_Success() throws Exception {
        // DTO
        Long roomId = 1L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest = RoomAppliancesUpdateRequest.of(1L, true,
                true, true, true, true, true, true, true);
        RoomDetailUpdateRequest roomDetailUpdateRequest = RoomDetailUpdateRequest.of(1L, (byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);

        RoomImageUpdateRequest roomImageUpdateRequest1 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", true);
        RoomImageUpdateRequest roomImageUpdateRequest2 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        RoomImageUpdateRequest roomImageUpdateRequest3 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        List<RoomImageUpdateRequest> roomImageUpdateRequestList = new ArrayList<>();
        roomImageUpdateRequestList.add(roomImageUpdateRequest1);
        roomImageUpdateRequestList.add(roomImageUpdateRequest2);
        roomImageUpdateRequestList.add(roomImageUpdateRequest3);

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.of(roomId, 37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesUpdateRequest, roomDetailUpdateRequest, roomImageUpdateRequestList);

        MockMultipartFile file0 = new MockMultipartFile("roomUpdateRequest", null, "application/json", objectMapper.writeValueAsString(roomUpdateRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ROOM_UPDATE_SUCCESS, NoneResponse.NONE);

        // mock
        given(roomServiceImpl.modifyRoom(any(), anyLong(), any(RoomUpdateRequest.class), any())).willReturn(success);

        // when
        mockMvc.perform(
                        multipart("/rooms/agents/{roomId}", String.valueOf(roomId))
                                .file(file0)
                                .file(file1)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(request -> {
                                    request.setMethod("PATCH"); // Override the method to PATCH
                                    return request;
                                })
                )

                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(ROOM_UPDATE_SUCCESS.name()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

    @Test
    @DisplayName("매물 수정 - 실패: 유효하지 않은 매물 ID")
    @WithMockUser
    public void modifyRoom_Fail_InvalidId() throws Exception {
        // DTO
        Long roomId = -1L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest = RoomAppliancesUpdateRequest.of(1L, true,
                true, true, true, true, true, true, true);
        RoomDetailUpdateRequest roomDetailUpdateRequest = RoomDetailUpdateRequest.of(1L, (byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);

        RoomImageUpdateRequest roomImageUpdateRequest1 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", true);
        RoomImageUpdateRequest roomImageUpdateRequest2 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        RoomImageUpdateRequest roomImageUpdateRequest3 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        List<RoomImageUpdateRequest> roomImageUpdateRequestList = new ArrayList<>();
        roomImageUpdateRequestList.add(roomImageUpdateRequest1);
        roomImageUpdateRequestList.add(roomImageUpdateRequest2);
        roomImageUpdateRequestList.add(roomImageUpdateRequest3);

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.of(roomId, 37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesUpdateRequest, roomDetailUpdateRequest, roomImageUpdateRequestList);

        MockMultipartFile file0 = new MockMultipartFile("roomUpdateRequest", null, "application/json", objectMapper.writeValueAsString(roomUpdateRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ROOM_UPDATE_SUCCESS, NoneResponse.NONE);

        // mock
        given(roomServiceImpl.modifyRoom(anyLong(), anyLong(), any(RoomUpdateRequest.class), any())).willReturn(success);

        // when
        mockMvc.perform(
                        multipart("/rooms/agents/{roomId}", String.valueOf(roomId))
                                .file(file0)
                                .file(file1)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(request -> {
                                    request.setMethod("PATCH"); // Override the method to PATCH
                                    return request;
                                })
                )

                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST.getMessage()));
    }

    @Test
    @DisplayName("매물 수정 - 실패: 유효하지 않은 매물 ID")
    @WithMockUser
    public void modifyRoom_Fail_invalidId() throws Exception {
        // DTO
        Long roomId = -1L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest = RoomAppliancesUpdateRequest.of(1L, true,
                true, true, true, true, true, true, true);
        RoomDetailUpdateRequest roomDetailUpdateRequest = RoomDetailUpdateRequest.of(1L, (byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);

        RoomImageUpdateRequest roomImageUpdateRequest1 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", true);
        RoomImageUpdateRequest roomImageUpdateRequest2 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        RoomImageUpdateRequest roomImageUpdateRequest3 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        List<RoomImageUpdateRequest> roomImageUpdateRequestList = new ArrayList<>();
        roomImageUpdateRequestList.add(roomImageUpdateRequest1);
        roomImageUpdateRequestList.add(roomImageUpdateRequest2);
        roomImageUpdateRequestList.add(roomImageUpdateRequest3);

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.of(roomId, 37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesUpdateRequest, roomDetailUpdateRequest, roomImageUpdateRequestList);

        MockMultipartFile file0 = new MockMultipartFile("roomUpdateRequest", null, "application/json", objectMapper.writeValueAsString(roomUpdateRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ROOM_UPDATE_SUCCESS, NoneResponse.NONE);

        // mock
        given(roomServiceImpl.modifyRoom(anyLong(), anyLong(), any(RoomUpdateRequest.class), any())).willReturn(success);

        // when
        mockMvc.perform(
                        multipart("/rooms/agents/{roomId}", String.valueOf(roomId))
                                .file(file0)
                                .file(file1)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(request -> {
                                    request.setMethod("PATCH"); // Override the method to PATCH
                                    return request;
                                })
                )

                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST.getMessage()));
    }

    @Test
    @DisplayName("매물 수정 - 실패: RoomId 누락")
    @WithMockUser
    public void modifyRoom_Fail_NoId() throws Exception {
        // DTO
        Long roomId = 1L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest = RoomAppliancesUpdateRequest.of(1L, true,
                true, true, true, true, true, true, true);
        RoomDetailUpdateRequest roomDetailUpdateRequest = RoomDetailUpdateRequest.of(1L, (byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);

        RoomImageUpdateRequest roomImageUpdateRequest1 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", true);
        RoomImageUpdateRequest roomImageUpdateRequest2 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        RoomImageUpdateRequest roomImageUpdateRequest3 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        List<RoomImageUpdateRequest> roomImageUpdateRequestList = new ArrayList<>();
        roomImageUpdateRequestList.add(roomImageUpdateRequest1);
        roomImageUpdateRequestList.add(roomImageUpdateRequest2);
        roomImageUpdateRequestList.add(roomImageUpdateRequest3);

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.of(roomId, 37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, 3000L, 10L, 10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesUpdateRequest, roomDetailUpdateRequest, roomImageUpdateRequestList);

        MockMultipartFile file0 = new MockMultipartFile("roomUpdateRequest", null, "application/json", objectMapper.writeValueAsString(roomUpdateRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ROOM_UPDATE_SUCCESS, NoneResponse.NONE);

        // mock
        given(roomServiceImpl.modifyRoom(anyLong(), anyLong(), any(RoomUpdateRequest.class), any())).willReturn(success);

        // when
        mockMvc.perform(
                        multipart("/rooms/agents")
                                .file(file0)
                                .file(file1)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(request -> {
                                    request.setMethod("PATCH"); // Override the method to PATCH
                                    return request;
                                })
                )

                //then
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value(METHOD_NOT_ALLOWED.getMessage()));
    }


    @Test
    @DisplayName("매물 수정 - 실패: 유효하지 않은 필드값")
    @WithMockUser
    public void modifyRoom_Fail_InvalidField() throws Exception {
        // DTO
        Long roomId = 1L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest = RoomAppliancesUpdateRequest.of(1L, true,
                true, true, true, true, true, true, true);
        RoomDetailUpdateRequest roomDetailUpdateRequest = RoomDetailUpdateRequest.of(1L, (byte) 1, (byte) 1,
                HeatingType.LOCAL, sdf.parse("2024-08-22"), 7L, true, 10L, 0.66f,
                sdf.parse("2020-04-11"), sdf.parse("2020-07-01"), Facing.SOUTH, Purpose.MULTI);

        RoomImageUpdateRequest roomImageUpdateRequest1 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", true);
        RoomImageUpdateRequest roomImageUpdateRequest2 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        RoomImageUpdateRequest roomImageUpdateRequest3 = RoomImageUpdateRequest.of(1L, roomId, "roomImageUrl", false);
        List<RoomImageUpdateRequest> roomImageUpdateRequestList = new ArrayList<>();
        roomImageUpdateRequestList.add(roomImageUpdateRequest1);
        roomImageUpdateRequestList.add(roomImageUpdateRequest2);
        roomImageUpdateRequestList.add(roomImageUpdateRequest3);

        RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.of(roomId, 37.5f, 127.039f, DealType.MONTHLY, RoomType.OFFICE, Structure.SEPERATED,
                true, 12.88f, 15.66f, (byte) 1, -3000L, -10L, -10L,
                "parcel", "서울시 강남구 역삼대로", "멀티캠퍼스 역삼", roomAppliancesUpdateRequest, roomDetailUpdateRequest, roomImageUpdateRequestList);

        MockMultipartFile file0 = new MockMultipartFile("roomUpdateRequest", null, "application/json", objectMapper.writeValueAsString(roomUpdateRequest).getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file1 = new MockMultipartFile("roomImageFile", "image1.png", "image/png", "image/png".getBytes());
        SuccessResponse<NoneResponse> success = new SuccessResponse<>(ROOM_UPDATE_SUCCESS, NoneResponse.NONE);

        // mock
        given(roomServiceImpl.modifyRoom(anyLong(), anyLong(), any(RoomUpdateRequest.class), any())).willReturn(success);

        // when
        mockMvc.perform(
                        multipart("/rooms/agents/{roomId}", String.valueOf(roomId))
                                .file(file0)
                                .file(file1)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(request -> {
                                    request.setMethod("PATCH"); // Override the method to PATCH
                                    return request;
                                })
                )

                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_REQUEST.getMessage()));
    }

    @Test
    @WithMockUser
    @DisplayName("중개인 매물 목록 조회 - 성공")
    public void getAgentRooms_Success() throws Exception {
        // DTO
        Long roomId = 1L;
        List<RoomInfoSearchResponse> roomInfos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            roomInfos.add(mock(RoomInfoSearchResponse.class));
        }
        SuccessResponse<List<RoomInfoSearchResponse>> successResponse = new SuccessResponse<>(ROOM_FIND_SUCCESS, roomInfos);

        // when
        when(roomServiceImpl.getAgentRooms(any(), anyInt(), anyInt())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                        get("/rooms/agents")
                                .param("page", "0")
                                .with(SecurityMockMvcRequestPostProcessors.csrf()
                                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ROOM_FIND_SUCCESS"));
    }


    @Test
    @WithMockUser
    @DisplayName("중개인 매물 목록 조회 - 성공: page defaultValue 0")
    public void getAgentRooms_Success_pageDefault() throws Exception {
        // DTO
        Long roomId = 1L;
        List<RoomInfoSearchResponse> roomInfos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            roomInfos.add(mock(RoomInfoSearchResponse.class));
        }
        SuccessResponse<List<RoomInfoSearchResponse>> successResponse = new SuccessResponse<>(ROOM_FIND_SUCCESS, roomInfos);

        // when
        when(roomServiceImpl.getAgentRooms(any(), anyInt(), anyInt())).thenReturn(successResponse);

        //then
        mockMvc.perform(
                        get("/rooms/agents")
                                .with(SecurityMockMvcRequestPostProcessors.csrf()
                                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ROOM_FIND_SUCCESS"));

    }
}
