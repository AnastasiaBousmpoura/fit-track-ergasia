package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/trainer/availability")
public class TrainerAvailabilityController {

    private final AppointmentService appointmentService;

    public TrainerAvailabilityController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/set")
    public String setAvailability(
            @RequestParam Long trainerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            RedirectAttributes redirectAttributes) {

        try {
            appointmentService.setTrainerAvailability(trainerId, start, end);
            redirectAttributes.addFlashAttribute("successMessage", "Το ωράριο αποθηκεύτηκε επιτυχώς!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Σφάλμα κατά την αποθήκευση: " + e.getMessage());
        }

        return "redirect:/trainers"; // Ή όπου θέλεις να ανακατευθύνεις
    }
}