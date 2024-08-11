package com.bangguddle.ownbang.domain.room.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@AllArgsConstructor
public enum Facing {
    EAST("동"),
    WEST("서"),
    SOUTH("남"),
    NORTH("북"),
    SOUTHEAST("남동"),
    SOUTHWEST("남서"),
    NORTHWEST("북서"),
    NORTHEAST("북동");

    private final String value;

    private static final Map<String, String> FACING_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(Facing::getValue, Facing::name))
    );

    @JsonCreator
    public static Facing fromValue(final String value) {
        String result = FACING_MAP.get(value);
        if (result == null) {
            throw new IllegalArgumentException("Invalid value '" + value + "'");
        }
        return Facing.valueOf(result);
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}

