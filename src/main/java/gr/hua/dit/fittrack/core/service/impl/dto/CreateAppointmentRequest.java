package gr.hua.dit.fittrack.core.service.impl.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull Long userId,
        @NotNull Long trainerId,
        @NotNull @Future LocalDateTime dateTime,
        @NotNull @NotBlank @Size(max = 50) String type,
        @Size(max = 255) String notes
) {
    // default constructor για Thymeleaf
    public CreateAppointmentRequest() {
        this(0L, 0L, LocalDateTime.now().plusDays(1), "", "");
    }
}
