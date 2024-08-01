package com.bangguddle.ownbang.domain.search.repository;

import com.bangguddle.ownbang.domain.search.entity.SearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchDocumentRepository extends ElasticsearchRepository<SearchDocument, String> {
    <S extends SearchDocument> Iterable<S> saveAll(Iterable<S> entities);
}