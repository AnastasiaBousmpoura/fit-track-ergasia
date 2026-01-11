package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.service.TrainerService;
import gr.hua.dit.fittrack.core.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    private final UserService userService;
    private final TrainerService trainerService;

    public ProfileController(final UserService userService,
                             final TrainerService trainerService) {
        this.userService = userService;
        this.trainerService = trainerService;
    }

    /**
     * Εμφάνιση Προφίλ μετά το Login.
     * Αν ο χρήστης είναι Trainer, ανακατευθύνεται στη λίστα των ραντεβού του.
     * Αν είναι απλός User, βλέπει τη σελίδα του προφίλ του.
     */
    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String email = authentication.getName();

        // Έλεγχος αν ο συνδεδεμένος χρήστης έχει ρόλο ROLE_TRAINER
        boolean isTrainer = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TRAINER"));

        if (isTrainer) {
            // Ανακατεύθυνση του Trainer στη διαχείριση των ραντεβού του
            return "redirect:/appointments/my-appointments";
        }

        // Λογική για απλό Χρήστη (User)
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Ο χρήστης με email " + email + " δεν βρέθηκε"));

        model.addAttribute("user", user);
        return "profile"; // Επιστρέφει το templates/profile.html
    }

    /**
     * Εμφάνιση δημόσιου προφίλ ενός Trainer (για τους επισκέπτες).
     */
    @GetMapping("/trainers/{id}")
    public String showTrainerProfile(@PathVariable Long id, Model model) {
        Trainer trainer = trainerService.findTrainerById(id)
                .orElseThrow(() -> new RuntimeException("Ο Trainer με ID " + id + " δεν βρέθηκε"));

        model.addAttribute("trainer", trainer);
        return "trainer-profile"; // Επιστρέφει το templates/trainer-profile.html
    }
}