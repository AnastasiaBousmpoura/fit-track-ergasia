package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.TrainerService;
import gr.hua.dit.fittrack.core.service.impl.TrainerServiceImpl;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TrainerService trainerService;

    public AppointmentController(
            final AppointmentService appointmentService,
            final TrainerService trainerService) {
        if (appointmentService == null) throw new NullPointerException();
        if (trainerService == null) throw new NullPointerException();

        this.appointmentService = appointmentService;
        this.trainerService = trainerService;
    }

    // Φόρμα για νέο ραντεβού
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Προετοιμασία του DTO για τη φόρμα
        model.addAttribute("appointmentRequest", new CreateAppointmentRequest(null, null, null, "","Athens"));
        // Λίστα trainers για το dropdown
        model.addAttribute("trainers", trainerService.findAllTrainers());
        return "appointments/create"; // appointments/create.html
    }

    // Επεξεργασία της φόρμας και εφαρμογή των κανόνων
    @PostMapping("/create")
    public String processCreate(
            @Valid @ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        // 1. Validation UI (π.χ. αν λείπουν πεδία ή η ημερομηνία δεν είναι @Future)
        if (bindingResult.hasErrors()) {
            model.addAttribute("trainers", trainerService.findAllTrainers());
            return "appointments/create";
        }

        // 2. Κλήση Service και έλεγχος των 3 κανόνων (Past date, Availability, Busy)
        CreateAppointmentResult result = appointmentService.createAppointment(request);

        if (!result.created()) {
            // Αν αποτύχει στέλνουμε το μήνυμα λάθους
            model.addAttribute("errorMessage", result.reason());
            model.addAttribute("trainers", trainerService.findAllTrainers());
            return "appointments/appointment-booking";
        }

        // 3. Επιτυχία - Ανακατεύθυνση στη λίστα
        return "redirect:/appointments/my-appointments?success";
    }

    @GetMapping("/api/appointments/my-appointments")
    public String listAppointments(Model model) {
        // ραντεβού χρήστη
        return "appointments/appointment-list";
    }
}