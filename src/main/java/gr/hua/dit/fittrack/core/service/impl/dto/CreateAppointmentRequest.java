package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull Long userId, // ID χρήστη που κλείνει το ραντεβού
        @NotNull Long trainerId, // ID του trainer
        @NotNull
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dateTime, // Ημερομηνία & ώρα ραντεβού
        @NotNull AppointmentType type, // Τύπος ραντεβού (π.χ. Indoor, outdoor)
        @Size(max = 255) String notes // Προαιρετικές σημειώσεις (μέχρι 255 χαρακτήρες)
) {

}
