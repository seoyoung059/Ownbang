package com.bangguddle.ownbang.domain.room.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Facing {
    EAST("동"), WEST("서"), SOUTH("남"), NORTH("북"), SOUTHEAST("남동"), SOUTHWEST("남서"), NORTHWEST("북서"), NORTHEAST("북동");

    private String value;

}
