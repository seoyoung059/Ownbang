package com.bangguddle.ownbang.domain.search.dto;

import com.bangguddle.ownbang.domain.search.entity.SearchType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchRequest {
    private String searchName;
    private String searchAddress;
    private SearchType searchType;

    public static SearchRequest from(SearchRequest doc) {
        return SearchRequest.builder()
                .searchName(doc.getSearchName())
                .searchAddress(doc.getSearchAddress())
                .searchType(doc.getSearchType())
                .build();
    }
}
