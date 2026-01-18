package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {


    CreateAppointmentResult createAppointment(CreateAppointmentRequest req, boolean notify);

    default CreateAppointmentResult createAppointment(CreateAppointmentRequest req) {
        return createAppointment(req, false);
    }

    void deleteAppointment(Long id);

    Optional<Appointment> findById(Long id);


    List<Appointment> getAppointmentsForTrainer(String email);

    List<Appointment> getAppointmentsByTrainer(String trainerEmail);
    List<Appointment> getAppointmentsByUser(String userEmail); // Πρόσθεσε και αυτήν αν λείπει

    void updateStatus(Long id, String status);


    void updateNotes(Long id, String notes);

    void cancelAppointment(Long appointmentId);

    List<LocalDateTime> getAvailableSlots(Long trainerId);
    void setTrainerAvailability(Long trainerId, LocalDateTime start, LocalDateTime end);




}