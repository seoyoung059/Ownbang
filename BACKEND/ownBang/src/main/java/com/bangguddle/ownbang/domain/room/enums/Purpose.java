package com.bangguddle.ownbang.domain.room.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Purpose {
    DETACHED("단독주택"), MULTI("공동주택"), BUSINESS("업무시설");

    private String value;
}
