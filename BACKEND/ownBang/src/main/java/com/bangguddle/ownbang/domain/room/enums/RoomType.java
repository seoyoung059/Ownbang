package com.bangguddle.ownbang.domain.room.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum RoomType {
    OFFICE("오피스텔"), FLAT("빌라");

    private String value;

    private static final Map<String, RoomType> ROOM_TYPE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(RoomType::getValue, Function.identity()))
    );


    @JsonCreator
    public static RoomType fromValue(String value) {
        RoomType result = ROOM_TYPE_MAP.get(value);
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
