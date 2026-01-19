package gr.hua.dit.fittrack.core.service.impl.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AddProgressRequest(

        @NotNull
        LocalDate date,   // Η ημερομηνία καταγραφής

        @NotNull
        @Positive
        Double weight,   // Βάρος χρήστη (πρέπει να είναι θετικό)

        @Size(max = 500)
        String notes     // Προαιρετικές σημειώσεις (μέχρι 500 χαρακτήρες)
) {}
