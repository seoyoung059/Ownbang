package com.bangguddle.ownbang.domain.room.enums;

import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@AllArgsConstructor
public enum HeatingType {
    INDIVIDUAL("개별"),
    CENTRAL("중앙"),
    LOCAL("지역");

    private final String value;

    private static final Map<String, String> HEATINGTYPE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(HeatingType::getValue, HeatingType::name))
    );

    @JsonCreator
    public static HeatingType fromValue(final String value) {
        String result = HEATINGTYPE_MAP.get(value);
        if (result == null) {
            throw new AppException(ErrorCode.INVALID_HEATINGTYPE);
        }
        return HeatingType.valueOf(result);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
