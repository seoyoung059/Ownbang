package com.bangguddle.ownbang.domain.room.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum HeatingType {
    INDIVIDUAL("개별"), CENTRAL("중앙"), LOCAL("지역");

    private String value;
}
