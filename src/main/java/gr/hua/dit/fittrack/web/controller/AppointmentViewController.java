package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.*;
import gr.hua.dit.fittrack.core.repository.TrainerAvailabilityRepository; // <--- ΠΡΟΣΘΗΚΗ
import gr.hua.dit.fittrack.core.repository.TrainerRepository; // <--- ΠΡΟΣΘΗΚΗ
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.TrainerService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentViewController {

    private final AppointmentService appointmentService;
    private final TrainerService trainerService;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository; // <--- ΠΡΟΣΘΗΚΗ
    private final TrainerAvailabilityRepository availabilityRepository; // <--- ΠΡΟΣΘΗΚΗ

    public AppointmentViewController(AppointmentService appointmentService,
                                     TrainerService trainerService,
                                     UserRepository userRepository,
                                     TrainerRepository trainerRepository,
                                     TrainerAvailabilityRepository availabilityRepository) {
        this.appointmentService = appointmentService;
        this.trainerService = trainerService;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.availabilityRepository = availabilityRepository;
    }

    // --- Λίστα ραντεβού για τον τρέχοντα χρήστη ή trainer ---
    @GetMapping("/my-appointments")
    public String listAppointments(Authentication authentication, Model model) {
        String email = authentication.getName();
        // Έλεγχος αν είναι trainer
        boolean isTrainer = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TRAINER"));

        if (isTrainer) {
            // Αν είναι trainer, φέρνουμε όλα τα ραντεβού που τον αφορούν
            model.addAttribute("appointments", appointmentService.getAppointmentsByTrainer(email));

            // Φέρνουμε τη διαθεσιμότητα για να τη δει ο Trainer στην ίδια σελίδα
            Trainer trainer = trainerRepository.findByEmail(email).orElseThrow();
            model.addAttribute("availabilities", availabilityRepository.findByTrainer_Id(trainer.getId()));

            return "trainer-availability";
        }

        // Αν είναι απλός χρήστης, εμφανίζουμε τα δικά του ραντεβού
        model.addAttribute("appointments", appointmentService.getAppointmentsByUser(email));
        return "appointment-list";
    }

    // ... οι υπόλοιπες μέθοδοι (new, processAppointment) μένουν όπως είναι ...
    // --- Φόρμα για δημιουργία νέου ραντεβού ---
    @GetMapping("/new")
    public String showCreateForm(@RequestParam(value = "trainerId", required = false) Long trainerId, Model model) {
        List<LocalDateTime> slots = new ArrayList<>();
        if (trainerId != null && trainerId > 0) {
            slots = appointmentService.getAvailableSlots(trainerId);
        }
        model.addAttribute("trainers", trainerService.findAllTrainers());
        model.addAttribute("availableSlots", slots);
        model.addAttribute("appointmentRequest", new CreateAppointmentRequest(0L, trainerId, null, AppointmentType.INDOOR, ""));
        return "appointment-booking";
    }

    // --- POST για επεξεργασία δημιουργίας ραντεβού ---
    @PostMapping("/new")
    public String processAppointment(@ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
                                     Authentication authentication, Model model) {
        // Παίρνουμε τον τρέχοντα χρήστη
        String email = authentication.getName();
        User user = userRepository.findByEmailAddress(email).orElseThrow();
        CreateAppointmentRequest fixed = new CreateAppointmentRequest(
                user.getId(), request.trainerId(), request.dateTime(), request.type(), request.notes());

        // Κλήση service για δημιουργία
        CreateAppointmentResult result = appointmentService.createAppointment(fixed, true);
        if (!result.created()) {
            model.addAttribute("errorMessage", result.reason());
            model.addAttribute("trainers", trainerService.findAllTrainers());
            model.addAttribute("availableSlots", appointmentService.getAvailableSlots(request.trainerId()));
            return "appointment-booking";
        }

        // Αν επιτύχει, redirect στη λίστα ραντεβού με μήνυμα επιτυχίας
        return "redirect:/appointments/my-appointments?success";
    }

    // --- Ακύρωση ραντεβού ---
    @PostMapping("/view/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id, Authentication authentication) {
        appointmentService.cancelAppointment(id);
        return "redirect:/appointments/my-appointments";
    }
}