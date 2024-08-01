package com.bangguddle.ownbang.domain.search.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "search_id", nullable = false, updatable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false)
    private String searchName;

    @Column(nullable = false)
    private String searchAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SearchType searchType;
}