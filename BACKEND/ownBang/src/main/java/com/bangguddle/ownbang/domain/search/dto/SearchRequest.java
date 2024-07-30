package com.bangguddle.ownbang.domain.search.dto;
import com.bangguddle.ownbang.domain.search.entity.Search;
import com.bangguddle.ownbang.domain.search.entity.SearchType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchRequest {
    @NotNull
    private String searchName;

    @NotNull
    private String searchAddress;

    @NotNull
    private SearchType searchType;

    public Search toEntity() {
        return Search.builder()
                .searchName(searchName)
                .searchAddress(searchAddress)
                .searchType(searchType)
                .build();
    }

    public static SearchRequest of(String searchName, String searchAddress, SearchType searchType) {
        return new SearchRequest(searchName, searchAddress, searchType);
    }

    public static SearchRequest from(Search search) {
        return new SearchRequest(
                search.getSearchName(),
                search.getSearchAddress(),
                search.getSearchType()
        );
    }
}
