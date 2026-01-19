package gr.hua.dit.fittrack.core.service.impl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TrainerRegistrationRequest(
        @NotBlank String firstName, // Όνομα trainer
        @NotBlank String lastName, // Επώνυμο trainer
        @NotBlank @Email String email, // Email σύνδεσης
        @NotBlank String password, // Κωδικός πρόσβασης
        @NotBlank String specialization, // Ειδικότητα trainer
        @NotBlank String area // Περιοχή δραστηριότητας
) {}