package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.model.entity.AppointmentType;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.TrainerService;
import gr.hua.dit.fittrack.core.service.UserService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TrainerService trainerService;
    private final UserService userService;
    private final TrainerRepository trainerRepository;


    public AppointmentController(final AppointmentService appointmentService,
                                 final TrainerService trainerService,
                                 final UserService userService,final TrainerRepository trainerRepository
) {
        if (appointmentService == null) throw new NullPointerException();
        if (trainerService == null) throw new NullPointerException();
        if(userService == null) throw new NullPointerException();

        this.appointmentService = appointmentService;
        this.trainerService = trainerService;
        this.userService = userService;
        this.trainerRepository = trainerRepository;
    }

    // --- Φόρμα για δημιουργία νέου ραντεβού ---
    @GetMapping("/create")
    public String showCreateForm(@RequestParam(value = "trainerId", required = false) Long trainerId,
                                 Model model,
                                 Authentication authentication) {

        // Παίρνουμε τον τρέχοντα user
        String email = authentication.getName();
        User currentUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Λίστα με όλους τους trainers για επιλογή στη φόρμα
        List<Trainer> trainers = trainerRepository.findAll();

        Trainer trainer = null;
        List<LocalDateTime> availableSlots = List.of();

        // Αν έχει δοθεί trainerId, παίρνουμε διαθέσιμες ημερομηνίες
        if (trainerId != null) {
            trainer = trainerService.findTrainerById(trainerId)
                    .orElseThrow(() -> new RuntimeException("Trainer not found"));
            availableSlots = appointmentService.getAvailableSlots(trainerId);
        }

        CreateAppointmentRequest request = new CreateAppointmentRequest(
                currentUser.getId(),
                trainerId,
                null, // Ημερομηνία θα επιλεγεί στη φόρμα
                AppointmentType.INDOOR,
                "" // Σημειώσεις προαιρετικά
        );

        // Προσθήκη δεδομένων
        model.addAttribute("appointmentRequest", request);
        model.addAttribute("trainers", trainers);
        model.addAttribute("trainer", trainer);
        model.addAttribute("availableSlots", availableSlots);

        return "create-appointment";
    }

    // --- Επεξεργασία POST για δημιουργία ραντεβού ---
    @PostMapping("/create")
    public String processCreate(@Valid @ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
                                BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("trainer", trainerService.findTrainerById(request.trainerId()).orElse(null));
            model.addAttribute("availableSlots", appointmentService.getAvailableSlots(request.trainerId()));
            return "create-appointment";
        }


        CreateAppointmentResult result = appointmentService.createAppointment(request, false);


        if (!result.created()) {// Αν αποτύχει, εμφανίζουμε μήνυμα σφάλματος
            model.addAttribute("errorMessage", result.reason());
            model.addAttribute("trainer", trainerService.findTrainerById(request.trainerId()).orElse(null));
            model.addAttribute("availableSlots", appointmentService.getAvailableSlots(request.trainerId()));
            return "create-appointment";
        }

        // Αν επιτύχει, redirect στο profile με success message
        redirectAttributes.addFlashAttribute("success", "Το ραντεβού σας καταχωρήθηκε επιτυχώς!");
        return "redirect:/profile";
    }

    // --- Ακύρωση ραντεβού ---
    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        appointmentService.cancelAppointment(id); // ή όπως το έχεις

        redirectAttributes.addFlashAttribute("success", "Το ραντεβού ακυρώθηκε.");
        return "redirect:/appointments/my-appointments";
    }

    // --- Προβολή σημειώσεων ενός ραντεβού ---
    @GetMapping("/{id}/notes")
    public String showNotes(@PathVariable Long id, Model model) {

        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        model.addAttribute("appointment", appointment);
        return "appointment-notes";
    }

    // --- Αποθήκευση σημειώσεων ραντεβού ---
    @PostMapping("/{id}/notes")
    public String saveNotes(@PathVariable Long id,
                            @RequestParam("notes") String notes,
                            RedirectAttributes redirectAttributes) {

        appointmentService.updateNotes(id, notes);

        redirectAttributes.addFlashAttribute("success",
                "Οι σημειώσεις αποθηκεύτηκαν επιτυχώς");
        return "redirect:/appointments/my-appointments";
    }

}