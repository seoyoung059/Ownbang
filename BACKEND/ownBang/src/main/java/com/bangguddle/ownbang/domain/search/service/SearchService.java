package com.bangguddle.ownbang.domain.search.service;

import com.bangguddle.ownbang.domain.search.dto.SearchListResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.stereotype.Service;

@Service
public interface SearchService {
    SuccessResponse<SearchListResponse> searchByName(String searchName);

}