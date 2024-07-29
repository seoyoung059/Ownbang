package com.bangguddle.ownbang.domain.room.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum Purpose {
    DETACHED("단독주택"), MULTI("공동주택"), BUSINESS("업무시설");

    private String value;

    private static final Map<String, Purpose> PURPOSE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(Purpose::getValue, Function.identity()))
    );


    @JsonCreator
    public static Purpose fromValue(String value) {
        Purpose result = PURPOSE_MAP.get(value);
        if (result == null) {
            throw new IllegalArgumentException("Invalid value '" + value + "'");
        }
        return result;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
