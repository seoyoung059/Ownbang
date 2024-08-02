package com.bangguddle.ownbang.domain.search.service;

import com.bangguddle.ownbang.domain.search.dto.SearchListResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

import java.io.IOException;

public interface SearchService {
    SuccessResponse<NoneResponse> syncDataFromMySQLToElasticsearch();
    SuccessResponse<SearchListResponse>searchByName(String name);
    SuccessResponse<NoneResponse> importExcelData() throws IOException;

}