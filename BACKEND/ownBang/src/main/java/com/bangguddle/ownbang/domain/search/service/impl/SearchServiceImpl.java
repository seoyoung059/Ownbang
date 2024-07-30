package com.bangguddle.ownbang.domain.search.service.impl;

import com.bangguddle.ownbang.domain.search.dto.SearchListResponse;
import com.bangguddle.ownbang.domain.search.entity.Search;
import com.bangguddle.ownbang.domain.search.repository.SearchDocumentRepository;
import com.bangguddle.ownbang.domain.search.service.SchoolDataParser;
import com.bangguddle.ownbang.domain.search.service.SearchService;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bangguddle.ownbang.global.enums.SuccessCode.SEARCH_LIST_SUCEESS;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchDocumentRepository searchDocumentRepository;

    private final SchoolDataParser schoolDataParser;

    public SuccessResponse<SearchListResponse> searchByName(String searchName) {
        List<Search> searches = searchDocumentRepository.findBySearchNameContaining(searchName);
        SearchListResponse searchListResponse = new SearchListResponse(searches);
        return new SuccessResponse<>(SEARCH_LIST_SUCEESS, searchListResponse);
    }

}
