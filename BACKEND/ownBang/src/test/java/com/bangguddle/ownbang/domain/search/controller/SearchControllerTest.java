package com.bangguddle.ownbang.domain.search.controller;

import com.bangguddle.ownbang.domain.search.dto.SearchListResponse;
import com.bangguddle.ownbang.domain.search.entity.SearchDocument;
import com.bangguddle.ownbang.domain.search.entity.SearchType;
import com.bangguddle.ownbang.domain.search.service.SearchService;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;

import static com.bangguddle.ownbang.global.enums.SuccessCode.SEARCH_LIST_SUCEESS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SearchController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})

class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Test
    @DisplayName("검색 - 성공")
    void searchByName_SUCCESS() throws Exception {
        // Given
        SearchDocument doc1 = new SearchDocument("1", "test1", "address1", SearchType.LOCATION);
        SearchDocument doc2 = new SearchDocument("2", "test2", "address2", SearchType.STATION);
        SearchListResponse searchListResponse = new SearchListResponse(Arrays.asList(doc1, doc2));
        SuccessResponse<SearchListResponse> successResponse = new SuccessResponse<>(SEARCH_LIST_SUCEESS, searchListResponse);

        when(searchService.searchByName(anyString())).thenReturn(successResponse);

        // When & Then
        mockMvc.perform(get("/api/search")
                        .param("searchName", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.searches[0].searchName").value("test1"))
                .andExpect(jsonPath("$.data.searches[1].searchName").value("test2"));
    }
}