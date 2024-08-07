package com.bangguddle.ownbang.domain.reservation.dto;

import java.time.LocalDate;

public record AvailableTimeRequest(Long roomId, LocalDate date) {}
