package com.bangguddle.ownbang.domain.room.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Structure {
    ONEROOM("원룸"), TWOROOM("투룸"), THREEROOM("쓰리룸"), SEPERATED("분리형");

    private String value;
}
