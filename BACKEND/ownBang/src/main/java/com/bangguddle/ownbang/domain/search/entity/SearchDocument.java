package com.bangguddle.ownbang.domain.search.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "search")
@Setting(settingPath = "/es-config/es-analyzer.json")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer", searchAnalyzer = "nori_analyzer")
    private String searchName;

    @Field(type = FieldType.Keyword)
    private String searchAddress;

    @Field(type = FieldType.Keyword)
    private SearchType searchType;
}