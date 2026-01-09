package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.service.AppointmentService;
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
public class AppointmentViewController {

    private final AppointmentService appointmentService;
    private final TrainerRepository trainerRepository;

    public AppointmentViewController(AppointmentService appointmentService, TrainerRepository trainerRepository) {
        this.appointmentService = appointmentService;
        this.trainerRepository = trainerRepository;
    }

    // 1. Εμφάνιση Φόρμας
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Χρησιμοποιούμε 1L για test αντί για null, για να μη "χτυπάει" το Service
        model.addAttribute("appointmentRequest", new CreateAppointmentRequest(1L, null, null, "","Athens"));

        // Φέρνουμε τους trainers για το dropdown list
        model.addAttribute("trainers", trainerRepository.findAll());

        return "appointment-booking"; // HTML στο templates/appointment-booking.html
    }

    // 2. Χειρισμός Υποβολής
    @PostMapping("/new")
    public String processAppointment(
            @Valid @ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        // Α) Validation UI (π.χ. αν ξεχάστηκε κάποιο πεδίο)
        if (bindingResult.hasErrors()) {
            model.addAttribute("trainers", trainerRepository.findAll());
            return "appointment-booking";
        }

        // Β) Εκτέλεση Logic (ΕΔΩ ΠΡΟΣΤΕΘΗΚΕ ΤΟ , true)
        CreateAppointmentResult result = appointmentService.createAppointment(request, true);

        if (!result.created()) {
            model.addAttribute("errorMessage", result.reason());
            model.addAttribute("trainers", trainerRepository.findAll());
            return "appointment-booking";
        }

        // Γ) Επιτυχία - Redirect στη λίστα
        return "redirect:/appointments/my-appointments?success";
    }

    // 3. Προβολή Λίστας
    @GetMapping("/my-appointments")
    public String listAppointments(Model model) {
        return "appointment-list"; // HTML στο templates/appointment-list.html
    }
}