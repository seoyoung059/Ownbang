package com.bangguddle.ownbang.domain.search.service.impl;

import com.bangguddle.ownbang.domain.search.dto.SearchListResponse;
import com.bangguddle.ownbang.domain.search.entity.Search;
import com.bangguddle.ownbang.domain.search.entity.SearchDocument;
import com.bangguddle.ownbang.domain.search.entity.SearchType;
import com.bangguddle.ownbang.domain.search.repository.SearchDocumentRepository;
import com.bangguddle.ownbang.domain.search.repository.SearchRepository;
import com.bangguddle.ownbang.domain.search.service.SearchService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bangguddle.ownbang.global.enums.SuccessCode.SEARCH_LIST_SUCEESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.SEARCH_SUCCESS;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchDocumentRepository searchDocumentRepository;
    private final SearchRepository searchRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * mysql 데이터를 일라스틱서치에 동기화
     * @return SuccessResponse
     */
    @PostConstruct
    public SuccessResponse<NoneResponse> syncDataFromMySQLToElasticsearch() {
        int batchSize = 1000;
        long totalCount = (long) entityManager.createQuery("SELECT COUNT(s) FROM Search s").getSingleResult();

        for (int i = 0; i < totalCount; i += batchSize) {
            List<Search> searches = entityManager.createQuery("SELECT s FROM Search s", Search.class)
                    .setFirstResult(i)
                    .setMaxResults(batchSize)
                    .getResultList();

            List<SearchDocument> documents = searches.stream()
                    .map(this::convertToSearchDocument)
                    .collect(Collectors.toList());

            searchDocumentRepository.saveAll(documents);
        }

        return new SuccessResponse<>(SEARCH_SUCCESS, NoneResponse.NONE);
    }

    private SearchDocument convertToSearchDocument(Search search) {
        return SearchDocument.builder()
                .id(search.getId().toString())
                .searchName(search.getSearchName())
                .searchAddress(search.getSearchAddress())
                .searchType(search.getSearchType())
                .build();
    }

    /**
     * 연관검색어 자동완성 API
     *
     * @param name 검색어
     * @return  SuccessResponse SearchListResponse
     */
    @Override
    public SuccessResponse<SearchListResponse> searchByName(String name) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.wildcardQuery("searchName.keyword", "*" + name + "*"))
                .withSort(SortBuilders.fieldSort("searchName.keyword").order(SortOrder.ASC)) // 이름순으로 정렬
                .build();

        SearchHits<SearchDocument> searchHits = elasticsearchOperations.search(searchQuery, SearchDocument.class);

        Map<String, SearchDocument> uniqueSearchesMap = new LinkedHashMap<>();
        searchHits.forEach(hit -> {
            SearchDocument doc = hit.getContent();
            uniqueSearchesMap.put(doc.getSearchName(), doc);
        });

        List<SearchDocument> uniqueSearches = new ArrayList<>(uniqueSearchesMap.values());

        uniqueSearches = uniqueSearches.stream()
                .sorted((a, b) -> compareSearchType(a.getSearchType(), b.getSearchType()))
                .limit(10)
                .collect(Collectors.toList());

        SearchListResponse searchListResponse = new SearchListResponse(uniqueSearches);
        return new SuccessResponse<>(SEARCH_LIST_SUCEESS, searchListResponse);
    }


    private int compareSearchType(SearchType a, SearchType b) {
        List<SearchType> order = List.of(SearchType.LOCATION, SearchType.STATION, SearchType.UNIVERSITY, SearchType.SCHOOL);
        return Integer.compare(order.indexOf(a), order.indexOf(b));
    }

    /**
     * 데이터 추가를 위해 excel을 mysql로 저장
     * @return SuccessResponse
     */
    public SuccessResponse<NoneResponse> importExcelData() throws IOException {
        try (FileInputStream file = new FileInputStream(new File("C:\\Users\\SSAFY\\Downloads\\지하철데이터.xlsx"));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 헤더 행 건너뛰기

                Cell nameCell = row.getCell(0);
                Cell addressCell = row.getCell(1);

                Search search = Search.builder()
                        .searchName(nameCell.getStringCellValue())
                        .searchAddress(addressCell.getStringCellValue())
                        .searchType(SearchType.STATION)
                        .build();

                searchRepository.save(search);
            }
        }
        return new SuccessResponse<>(SEARCH_SUCCESS, NoneResponse.NONE);
    }

}
