package gr.hua.dit.fittrack.core.service.impl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

    public record RegisterDto(
        @NotNull @NotBlank String role, // ρόλος (π.χ. USER / TRAINER)
        @NotNull @NotBlank String email, // email χρήστη
        @NotNull @NotBlank String password, // κωδικός
        @NotNull @NotBlank String username // username
    ){}
