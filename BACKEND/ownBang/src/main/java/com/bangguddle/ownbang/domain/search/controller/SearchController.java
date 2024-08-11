package com.bangguddle.ownbang.domain.search.controller;

import com.bangguddle.ownbang.domain.search.dto.SearchListResponse;
import com.bangguddle.ownbang.domain.search.service.SearchService;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    /**
     * 연관검색어 자동완성
     *
     * @param name 검색어
     * @return SuccessResponse SearchListResponse, 실패 시 AppException Throw
     */
    @GetMapping
    public ResponseEntity<Response<SearchListResponse>> searchByName(@RequestParam(name="searchName") String name) {
        SuccessResponse<SearchListResponse> response = searchService.searchByName(name);
        return Response.success(response);
    }
}