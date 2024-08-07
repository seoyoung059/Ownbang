package com.bangguddle.ownbang.domain.checklist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record ChecklistUpdateRequest(
        @NotBlank
        String title,

        @NotNull
        Map<String, String> contents
){

    public String contentsToString() {
        return contents.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(",\n"));
    }
}
