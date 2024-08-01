package com.bangguddle.ownbang.domain.search.service;

import com.bangguddle.ownbang.domain.search.dto.SearchListResponse;
import com.bangguddle.ownbang.domain.search.entity.SearchDocument;
import com.bangguddle.ownbang.domain.search.entity.SearchType;
import com.bangguddle.ownbang.domain.search.service.impl.SearchServiceImpl;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.Arrays;
import java.util.List;

import static com.bangguddle.ownbang.global.enums.SuccessCode.SEARCH_LIST_SUCEESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SearchServiceImplTest {

    @InjectMocks
    private SearchServiceImpl searchService;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchByName_ShouldReturnSuccessResponse() {
        // Given
        String searchTerm = "test";
        SearchDocument doc1 = new SearchDocument("1", "test1", "address1", SearchType.LOCATION);
        SearchDocument doc2 = new SearchDocument("2", "test2", "address2", SearchType.STATION);
        SearchDocument doc3 = new SearchDocument("3", "test1", "address3", SearchType.LOCATION); // 중복 이름

        SearchHit<SearchDocument> hit1 = new SearchHit<>("1", 1.0f, null, null, doc1);
        SearchHit<SearchDocument> hit2 = new SearchHit<>("2", 1.0f, null, null, doc2);
        SearchHit<SearchDocument> hit3 = new SearchHit<>("3", 1.0f, null, null, doc3);

        List<SearchHit<SearchDocument>> searchHitsList = Arrays.asList(hit1, hit2, hit3);

        SearchHits<SearchDocument> mockSearchHits = mock(SearchHits.class);
        when(mockSearchHits.getSearchHits()).thenReturn(searchHitsList);

        when(elasticsearchOperations.search(any(NativeSearchQuery.class), eq(SearchDocument.class)))
                .thenReturn(mockSearchHits);

        ArgumentCaptor<NativeSearchQuery> queryCaptor = ArgumentCaptor.forClass(NativeSearchQuery.class);

        SuccessResponse<SearchListResponse> response = searchService.searchByName(searchTerm);

        verify(elasticsearchOperations).search(queryCaptor.capture(), eq(SearchDocument.class));

        NativeSearchQuery capturedQuery = queryCaptor.getValue();

        assertTrue(capturedQuery.getQuery().toString().contains("searchName.keyword:*test*"));

        assertNotNull(response);
        assertEquals(SEARCH_LIST_SUCEESS, response.successCode());
        assertNotNull(response.data());

        List<SearchDocument> resultSearches = response.data().searches();
        assertEquals(2, resultSearches.size()); // 중복 제거 후 2개만 남아야 함
        assertEquals("test1", resultSearches.get(0).getSearchName());
        assertEquals("test2", resultSearches.get(1).getSearchName());

        assertTrue(compareSearchType(resultSearches.get(0).getSearchType(), resultSearches.get(1).getSearchType()) <= 0);
    }

    private int compareSearchType(SearchType a, SearchType b) {
        if (a == b) return 0;
        if (a == SearchType.LOCATION) return -1;
        if (b == SearchType.LOCATION) return 1;
        if (a == SearchType.STATION) return -1;
        return 1;
    }
}