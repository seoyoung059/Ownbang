package com.bangguddle.ownbang.domain.search.service;

import com.bangguddle.ownbang.domain.search.dto.SearchListResponse;
import com.bangguddle.ownbang.domain.search.entity.SearchDocument;
import com.bangguddle.ownbang.domain.search.entity.SearchType;
import com.bangguddle.ownbang.domain.search.service.impl.SearchServiceImpl;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.bangguddle.ownbang.global.enums.SuccessCode.SEARCH_LIST_SUCEESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SearchServiceImplTest {

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @InjectMocks
    private SearchServiceImpl searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchByName_Success() {
        // Given
        String searchName = "test";
        when(elasticsearchOperations.search(any(NativeSearchQuery.class), eq(SearchDocument.class)))
                .thenReturn(createMockSearchHits());

        // When
        SuccessResponse<SearchListResponse> response = searchService.searchByName(searchName);

        // Then
        assertNotNull(response);
        assertEquals(SEARCH_LIST_SUCEESS, response.successCode());
        assertNotNull(response.data());

        SearchListResponse searchListResponse = response.data();
        assertNotNull(searchListResponse.getSearches());
        assertFalse(searchListResponse.getSearches().isEmpty());
        assertTrue(searchListResponse.getSearches().size() <= 10);
    }

    private SearchHits<SearchDocument> createMockSearchHits() {
        List<SearchDocument> documents = Arrays.asList(
                new SearchDocument("1", "테스트1", "주소1", SearchType.LOCATION),
                new SearchDocument("2", "테스트2", "주소2", SearchType.LOCATION),
                new SearchDocument("3", "테스트3", "주소3", SearchType.LOCATION)
        );

        List<SearchHit<SearchDocument>> searchHits = documents.stream()
                .map(doc -> new SearchHit<>(
                        doc.getId(),
                        1.0f,
                        null,
                        null,
                        doc))
                .collect(Collectors.toList());

        return new SearchHitsImpl<>(
                3L,
                TotalHitsRelation.EQUAL_TO,
                10.0f,
                null,
                searchHits,
                null
        );
    }
}