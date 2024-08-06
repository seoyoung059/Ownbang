package com.bangguddle.ownbang.domain.checklist.dto;

import com.bangguddle.ownbang.domain.checklist.entity.Checklist;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Map;
import java.util.stream.Collectors;

public record ChecklistCreateRequest(
        @NotNull
        @Positive
        Long reservationId,

        @NotBlank
        String title,

        @NotNull
        Map<String, String> contents
){
    public Checklist toEntity(User user, Reservation reservation){
        return Checklist.builder()
                .user(user)
                .reservation(reservation)
                .title(title)
                .contents(contentsToString())
                .isTemplate(false)
                .build();
    }

    public String contentsToString(){
        return this.contents.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(",\n"));
    }
}