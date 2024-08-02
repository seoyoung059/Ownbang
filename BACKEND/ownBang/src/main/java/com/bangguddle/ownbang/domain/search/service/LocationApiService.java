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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class LocationApiService {

    @Value("${location.api.key}")
    private String apiKey;

    private final SearchRepository searchRepository;


    private static final String API_URL = "https://api.odcloud.kr/api/15063424/v1/uddi:257e1510-0eeb-44de-8883-8295c94dadf7";


    public SuccessResponse<NoneResponse> getLocationInfo() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Search> allLocations = new ArrayList<>();

        int page = 1;
        boolean hasMoreData = true;
        int PAGE_SIZE = 1000;
        try {
            while (hasMoreData) {
                // URL 조합
                String url = API_URL + "?page=" + page +
                        "&perPage=" + PAGE_SIZE +
                        "&serviceKey=" + apiKey;

//                System.out.println("Request URL: " + url); // URL 로그 출력

                // API 요청
                URI uri = new URI(url);
                String jsonResponse = restTemplate.getForObject(uri, String.class);

                JsonNode root = objectMapper.readTree(jsonResponse);
                JsonNode data = root.path("data");

                if (!data.isArray() || data.size() == 0) {
                    hasMoreData = false;
                } else {
                    for (JsonNode location : data) {
                        String sidoName = location.path("시도명").asText("");
                        String sigunguName = location.path("시군구명").asText("");
                        String eupmyeondongName = location.path("읍면동명").asText("");

                        String locationAddress = combineAddress(sidoName, sigunguName, eupmyeondongName);
                        String locationName = determineName(sidoName, sigunguName, eupmyeondongName);

                        Search searchEntity = Search.builder()
                                .searchName(locationName)
                                .searchAddress(locationAddress)
                                .searchType(SearchType.LOCATION)
                                .build();

                        allLocations.add(searchEntity);
                    }
                    page++;
                }
            }

            searchRepository.saveAll(allLocations);
            return new SuccessResponse<>(SuccessCode.SEARCH_SUCCESS, NoneResponse.NONE);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(BAD_REQUEST);
        }
    }

    private String combineAddress(String sidoName, String sigunguName, String eupmyeondongName) {
        StringBuilder address = new StringBuilder();
        if (!sidoName.isEmpty()) address.append(sidoName);
        if (!sigunguName.isEmpty()) address.append(" ").append(sigunguName);
        if (!eupmyeondongName.isEmpty()) address.append(" ").append(eupmyeondongName);
        return address.toString().trim();
    }

    private String determineName(String sidoName, String sigunguName, String eupmyeondongName) {
        if (!eupmyeondongName.isEmpty()) return eupmyeondongName;
        if (!sigunguName.isEmpty()) return sigunguName;
        return sidoName;
    }
}
