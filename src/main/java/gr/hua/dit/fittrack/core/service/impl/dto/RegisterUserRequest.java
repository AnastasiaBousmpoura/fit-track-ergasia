package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank @Email String email, // email χρήστη
        @NotBlank @Size(min = 4, max = 24) String password, // κωδικός
        @NotBlank String firstName, // όνομα
        @NotBlank String lastName, // επώνυμο
        String fitnessGoal, // στόχος fitness

        @NotNull Role role, // ρόλος (USER / TRAINER)
        String specialization, // ειδικότητα trainer
        String area // περιοχή trainer
) {}
