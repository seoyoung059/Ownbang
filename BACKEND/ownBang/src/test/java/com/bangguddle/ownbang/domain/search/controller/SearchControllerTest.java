package com.bangguddle.ownbang.domain.search.controller;

import com.bangguddle.ownbang.domain.search.dto.SearchListResponse;
import com.bangguddle.ownbang.domain.search.entity.SearchDocument;
import com.bangguddle.ownbang.domain.search.entity.SearchType;
import com.bangguddle.ownbang.domain.search.service.SearchService;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;
import java.util.List;

import static com.bangguddle.ownbang.global.enums.SuccessCode.SEARCH_LIST_SUCEESS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SearchController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})
        })
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Test
    public void testSearchByName() throws Exception {
        String searchName = "테스트";

        List<SearchDocument> mockDocuments = Arrays.asList(
                new SearchDocument("1", "테스트1", "주소1", SearchType.LOCATION),
                new SearchDocument("2", "테스트2", "주소2", SearchType.LOCATION),
                new SearchDocument("3", "테스트3", "주소3", SearchType.LOCATION)
        );

        SearchListResponse mockResponse = new SearchListResponse(mockDocuments);
        SuccessResponse<SearchListResponse> successResponse = new SuccessResponse<>(SEARCH_LIST_SUCEESS, mockResponse);

        when(searchService.searchByName(anyString())).thenReturn(successResponse);

        mockMvc.perform(get("/search")
                        .param("searchName", searchName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(SEARCH_LIST_SUCEESS.name()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.searches[0].id").value("1"))
                .andExpect(jsonPath("$.data.searches[1].id").value("2"))
                .andExpect(jsonPath("$.data.searches[2].id").value("3"));
    }
}

