package com.bangguddle.ownbang.domain.checklist.dto;

import com.bangguddle.ownbang.domain.checklist.entity.Checklist;
import lombok.Builder;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record ChecklistSearchResponse(
        Long checklistId,
        String title,
        Map<String,String> contents,
        Boolean isTemplate
) {
    static public ChecklistSearchResponse from(Checklist checklist){
        Map<String, String> contentsToMap = Arrays.stream(checklist.getContents().split(",\n"))
                .map(entry -> entry.split(": ", 2))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(parts -> parts[0].trim(), parts -> parts[1].trim()));

        return ChecklistSearchResponse.builder()
                .checklistId(checklist.getId())
                .contents(contentsToMap)
                .title(checklist.getTitle())
                .isTemplate(checklist.getIsTemplate())
                .build();
    }
}
