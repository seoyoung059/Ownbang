package com.bangguddle.ownbang.domain.search.service;

import com.bangguddle.ownbang.domain.search.entity.Search;
import com.bangguddle.ownbang.domain.search.entity.SearchType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SchoolDataParser {

    public List<Search> parseSchoolData(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonResponse);

        List<Search> searches = new ArrayList<>();

        JsonNode schoolInfo = root.path("schoolInfo").get(1).path("row");
        for (JsonNode school : schoolInfo) {
            Search search = Search.builder()
                    .searchName(school.path("SCHUL_NM").asText())
                    .searchAddress(school.path("ORG_RDNMA").asText())
                    .searchType(SearchType.SCHOOL)
                    .build();
            searches.add(search);
        }

        return searches;
    }
}