package gr.hua.dit.fittrack.web.rest;

import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;


@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    private final AppointmentService appointmentService;

    public AppointmentRestController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // --- Δημιουργία ραντεβού (POST /api/appointments) ---
    @Operation(summary = "Create appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Validation/Business error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\":\"2026-01-10T00:00:00Z\",\"status\":400,\"error\":\"validation_error\",\"message\":\"Validation failed\",\"path\":\"/api/appointments\",\"fields\":{\"dateTime\":\"must be a future date\"}}"
                            ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\":\"2026-01-10T00:00:00Z\",\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Full authentication is required\",\"path\":\"/api/appointments\",\"fields\":null}"
                            ))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\":\"2026-01-10T00:00:00Z\",\"status\":403,\"error\":\"Forbidden\",\"message\":\"Access is denied\",\"path\":\"/api/appointments\",\"fields\":null}"
                            )))
    })

    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        CreateAppointmentResult result = appointmentService.createAppointment(request);

        if (result.created()) {
            return ResponseEntity.ok(result.appointment());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.reason());
    }

    // --- Διαγραφή ραντεβού (DELETE /api/appointments/{id}) ---
    @Operation(summary = "Delete appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\":\"2026-01-10T00:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Appointment not found\",\"path\":\"/api/appointments/123\",\"fields\":null}"
                            ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\":\"2026-01-10T00:00:00Z\",\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Full authentication is required\",\"path\":\"/api/appointments/123\",\"fields\":null}"
                            )))
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok(Map.of("message", "Το ραντεβού ακυρώθηκε με επιτυχία."));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
