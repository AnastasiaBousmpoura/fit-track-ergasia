package gr.hua.dit.fittrack.core.controller;

import jakarta.validation.constraints.NotBlank;

public record TrainerNotesForm(
        @NotBlank String text
) {}