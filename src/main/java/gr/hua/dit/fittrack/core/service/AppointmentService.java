package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    // Δημιουργία ραντεβού
    CreateAppointmentResult createAppointment(CreateAppointmentRequest req, boolean notify);

    // Υπέρβαση με default για πιο απλή χρήση
    default CreateAppointmentResult createAppointment(CreateAppointmentRequest req) {
        return createAppointment(req, false);
    }

    // Διαγραφή ραντεβού
    void deleteAppointment(Long id);

    // Λίστα διαθέσιμων slots για trainer
    List<LocalDateTime> getAvailableSlots(Long trainerId);

    void setTrainerAvailability(Long trainerId, LocalDateTime start, LocalDateTime end);

}
