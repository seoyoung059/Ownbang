package com.bangguddle.ownbang.domain.room.enums;

import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum Structure {
    ONEROOM("원룸"), TWOROOM("투룸"), THREEROOM("쓰리룸"), SEPERATED("분리형");

    private String value;

    private static final Map<String, Structure> STRUCTURE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(Structure::getValue, Function.identity()))
    );


    @JsonCreator
    public static Structure fromValue(String value) {
        Structure result = STRUCTURE_MAP.get(value);
        if (result == null) {
            throw new AppException(ErrorCode.INVALID_STRUCTURE);
        }
        return result;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
