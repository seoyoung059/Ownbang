package com.bangguddle.ownbang.domain.search.service;

import com.bangguddle.ownbang.domain.search.entity.Search;
import com.bangguddle.ownbang.domain.search.entity.SearchType;
import com.bangguddle.ownbang.domain.search.repository.SearchRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class SchoolApiService {

    @Value("${school.api.key}")
    private String apiKey;
    private final SearchRepository searchRepository;
    private final String API_URL = "https://open.neis.go.kr/hub/schoolInfo";

    public SuccessResponse<NoneResponse> getSchoolInfo() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Search> allSchools = new ArrayList<>();

        int pIndex = 1;
        int pSize = 1000;
        boolean hasMoreData = true;

        try {
            while (hasMoreData) {
                String url = API_URL + "?KEY=" + apiKey + "&Type=json&pIndex=" + pIndex + "&pSize=" + pSize;
                String jsonResponse = restTemplate.getForObject(url, String.class);

                JsonNode root = objectMapper.readTree(jsonResponse);
                JsonNode schoolInfo = root.path("schoolInfo");

                if (!schoolInfo.isArray() || schoolInfo.size() < 2) {
                    hasMoreData = false;
                    continue;
                }

                JsonNode rowNode = schoolInfo.get(1).path("row");

                if (!rowNode.isArray() || rowNode.size() == 0) {
                    hasMoreData = false;
                } else {
                    for (JsonNode school : rowNode) {
                        String schoolName = school.path("SCHUL_NM").asText();
                        String schoolAddress = school.path("ORG_RDNMA").asText(); // 도로명 주소

                        Search searchEntity = Search.builder()
                                .searchName(schoolName)
                                .searchAddress(schoolAddress)
                                .searchType(SearchType.SCHOOL)
                                .build();

                        allSchools.add(searchEntity);
                    }
                    pIndex++;
                }
            }

            searchRepository.saveAll(allSchools);
            return new SuccessResponse<>(SuccessCode.SEARCH_SUCCESS, NoneResponse.NONE);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(BAD_REQUEST);
        }
    }
}
