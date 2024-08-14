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
public enum DealType {
    MONTHLY("월세"), JEONSE("전세");

    private final String value;

    private static final Map<String, DealType> DEALTYPE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(DealType::getValue, Function.identity()))
    );

    @JsonCreator
    public static DealType fromValue(String value) {
        DealType result = DEALTYPE_MAP.get(value);
        if (result == null) {
            throw new AppException(ErrorCode.INVALID_DEALTYPE);
        }
        return result;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
