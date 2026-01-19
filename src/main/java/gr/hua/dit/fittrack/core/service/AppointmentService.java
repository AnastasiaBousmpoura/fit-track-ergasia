package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    // Δημιουργεί νέο ραντεβού.
    CreateAppointmentResult createAppointment(CreateAppointmentRequest req, boolean notify);

    default CreateAppointmentResult createAppointment(CreateAppointmentRequest req) {
        return createAppointment(req, false);
    }

    // Διαγράφει ραντεβού με βάση το ID.
    void deleteAppointment(Long id);

    //Βρίσκει ραντεβού με βάση το ID.
    Optional<Appointment> findById(Long id);

    // Επιστρέφει όλα τα ραντεβού για trainer με συγκεκριμένο email.
    List<Appointment> getAppointmentsForTrainer(String email);

    //Επιστρέφει όλα τα ραντεβού για trainer/user με βάση email.
    List<Appointment> getAppointmentsByTrainer(String trainerEmail);
    List<Appointment> getAppointmentsByUser(String userEmail); // Πρόσθεσε και αυτήν αν λείπει

    // Ενημερώνει την κατάσταση ενός ραντεβού.
    void updateStatus(Long id, String status);

    // Ενημερώνει τις σημειώσεις ενός ραντεβού.
    void updateNotes(Long id, String notes);

    //Ακυρώνει ένα ραντεβού.
    void cancelAppointment(Long appointmentId);

    //Επιστρέφει και ορίζει διαθεσιμότητα trainer σε συγκεκριμένη χρονική περίοδο
    List<LocalDateTime> getAvailableSlots(Long trainerId);
    void setTrainerAvailability(Long trainerId, LocalDateTime start, LocalDateTime end);

}