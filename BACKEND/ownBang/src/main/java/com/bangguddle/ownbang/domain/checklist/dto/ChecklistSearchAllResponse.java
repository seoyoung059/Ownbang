package com.bangguddle.ownbang.domain.checklist.dto;

import com.bangguddle.ownbang.domain.checklist.entity.Checklist;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ChecklistSearchAllResponse(
        Integer count,
        List<ChecklistSearchResponse> data
) {

    public static ChecklistSearchAllResponse from(List<Checklist> checklists) {
        List<ChecklistSearchResponse> responses = checklists.stream()
                .map(ChecklistSearchResponse::from)
                .collect(Collectors.toList());

        return new ChecklistSearchAllResponse(responses.size(), responses);
    }
}
