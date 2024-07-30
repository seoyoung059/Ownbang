package com.bangguddle.ownbang.domain.search.dto;

import com.bangguddle.ownbang.domain.search.entity.Search;

import java.util.List;

public record SearchListResponse(
        List<Search> searches
){
}
