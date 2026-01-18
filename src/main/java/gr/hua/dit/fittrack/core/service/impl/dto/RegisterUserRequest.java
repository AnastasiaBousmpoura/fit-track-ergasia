package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 4, max = 24) String password,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String fitnessGoal,

        @NotNull Role role,
        String specialization,
        String area
) {}
