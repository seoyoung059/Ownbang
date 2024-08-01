package com.bangguddle.ownbang.domain.search.dto;

import com.bangguddle.ownbang.domain.search.entity.SearchDocument;

import java.util.List;

public record SearchListResponse(
        List<SearchDocument> searches
) {
}