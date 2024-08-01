package com.bangguddle.ownbang.domain.search.service;

import com.bangguddle.ownbang.domain.search.entity.Search;
import com.bangguddle.ownbang.domain.search.entity.SearchType;
import com.bangguddle.ownbang.domain.search.repository.SearchRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
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

@Service
@RequiredArgsConstructor
public class UniversityApiService {

    @Value("${university.api.key}")
    private String apiKey;

    private final String baseUrl = "https://www.career.go.kr/cnet/openapi/getOpenApi.json";

    private final SearchRepository searchRepository;


    public SuccessResponse<NoneResponse> getUniversityInfo() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Search> allLocations = new ArrayList<>();
        int pageIndex = 1;
        boolean hasMoreData = true;

        while (hasMoreData) {
            String url = String.format("%s?apiKey=%s&svcType=api&svcCode=SCHOOL&contentType=json&gubun=univ_list&pageIndex=%d", baseUrl, apiKey, pageIndex);
            String response = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode dataList = root.path("dataSearch").path("content");

            if (dataList.isEmpty()) {
                hasMoreData = false;
            } else {
                for (JsonNode data : dataList) {
                    String sch2 = data.path("sch2").asText();
                    if (!"사이버대학(2년제)".equals(sch2) && !"사이버대학(4년제)".equals(sch2)) {
                        String schoolName = data.path("schoolName").asText();
                        String adres = data.path("adres").asText();

                        Search search = Search.builder()
                                .searchName(schoolName)
                                .searchAddress(adres)
                                .searchType(SearchType.UNIVERSITY)
                                .build();

                        searchRepository.save(search);
                    }
                }
                pageIndex++;
            }
        }
        return new SuccessResponse<>(SuccessCode.SEARCH_SUCCESS, NoneResponse.NONE);
    }
}