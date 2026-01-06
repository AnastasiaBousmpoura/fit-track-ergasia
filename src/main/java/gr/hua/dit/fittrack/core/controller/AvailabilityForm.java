package gr.hua.dit.fittrack.core.controller;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AvailabilityForm(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {}