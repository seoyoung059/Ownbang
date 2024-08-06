package com.bangguddle.ownbang.domain.checklist.dto;

import com.bangguddle.ownbang.domain.checklist.entity.Checklist;
import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record ChecklistTemplateCreateRequest(
        @NotBlank
        String title,

        @NotNull
        Map<String, String> contents
){
        public Checklist toEntity(User user){
                String contentsToString = contents.entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + ": " + entry.getValue())
                        .collect(Collectors.joining(",\n"));

                return Checklist.builder()
                        .user(user)
                        .title(title)
                        .contents(contentsToString)
                        .isTemplate(true)
                        .build();
        }
}
