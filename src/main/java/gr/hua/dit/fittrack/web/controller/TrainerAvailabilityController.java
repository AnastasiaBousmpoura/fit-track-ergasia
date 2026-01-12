package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Προσθήκη
import org.springframework.web.bind.annotation.GetMapping; // Προσθήκη
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/trainer/availability")
public class TrainerAvailabilityController {

    private final AppointmentService appointmentService;
    private final TrainerRepository trainerRepository;

    public TrainerAvailabilityController(AppointmentService appointmentService, TrainerRepository trainerRepository) {
        this.appointmentService = appointmentService;
        this.trainerRepository = trainerRepository;
    }

    // 1. Εμφάνιση της φόρμας
    @GetMapping
    public String showAvailabilityForm() {
        return "set-availability"; // Το όνομα του HTML αρχείου σας
    }

    // 2. Επεξεργασία της φόρμας
    @PostMapping("/set")
    public String setAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            // Παίρνουμε το email του συνδεδεμένου Trainer
            String email = authentication.getName();

            // ΠΡΟΣΟΧΗ: Βεβαιώσου ότι στο TrainerRepository η μέθοδος είναι findByEmail
            // ή findByEmailAddress ανάλογα με το entity σου
            Trainer trainer = trainerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Ο Trainer δεν βρέθηκε στο σύστημα."));

            // Έλεγχος λογικής: Η έναρξη πρέπει να είναι πριν τη λήξη
            if (start.isAfter(end)) {
                throw new RuntimeException("Η ώρα έναρξης πρέπει να είναι πριν την ώρα λήξης.");
            }

            // Έλεγχος: Όχι διαθεσιμότητα στο παρελθόν
            if (start.isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Δεν μπορείτε να ορίσετε διαθεσιμότητα σε παρελθοντική ώρα.");
            }

            appointmentService.setTrainerAvailability(trainer.getId(), start, end);
            redirectAttributes.addFlashAttribute("successMessage", "Το ωράριο αποθηκεύτηκε επιτυχώς!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Σφάλμα: " + e.getMessage());
        }

        return "redirect:/appointments/my-appointments";
    }
}