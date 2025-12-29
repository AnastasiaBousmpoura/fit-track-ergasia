package gr.hua.dit.fittrack.core.service.impl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

    public record RegisterDto(
        @NotNull @NotBlank String role,
        @NotNull @NotBlank String email,
        @NotNull @NotBlank String password,
        @NotNull @NotBlank String username
    ){}
