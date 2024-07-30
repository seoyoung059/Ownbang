package com.bangguddle.ownbang.domain.search.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "search")
@Setting(settingPath = "es-config/es-analyzer.json")
@AllArgsConstructor
@Builder
@Entity
@Table(name = "search")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchDocument {

    @Id
    @Field(type = FieldType.Long)
    @Column(name = "search_id",nullable = false)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    @Column(nullable = false)
    private String searchName;

    @Field(type = FieldType.Text)
    @Column(nullable = false)
    private String searchAddress;

    @Field(type = FieldType.Keyword)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SearchType searchType;

    public static SearchDocument from (Search search) {
        return SearchDocument.builder()
                .searchName(search.getSearchName())
                .searchAddress(search.getSearchAddress())
                .searchType(search.getSearchType())
                .build();
    }
}