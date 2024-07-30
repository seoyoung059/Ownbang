package com.bangguddle.ownbang.domain.search.repository;

import com.bangguddle.ownbang.domain.search.entity.Search;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchDocumentRepository extends ElasticsearchRepository<Search, Long> {
    List<Search> findBySearchNameContaining(String searchName);

}