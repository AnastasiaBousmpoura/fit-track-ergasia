package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.UserService;
import gr.hua.dit.fittrack.web.dto.ProgressForm;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Controller
@RequestMapping("/progress/add")
public class ProgressController {

    private final UserService userService;
    private final UserRepository userRepository;

    public ProgressController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // --- Φόρμα για προσθήκη προόδου ---
    @GetMapping
    public String showProgressForm(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProgressForm form = new ProgressForm();
        form.setWeight(user.getCurrentWeight());
        form.setRunningTime(user.getRunningTime());
        form.setDate(LocalDate.now());

        model.addAttribute("progressForm", form);
        model.addAttribute("user", user);
        return "progress"; // Επιστρέφει το αρχείο progress.html
    }

    // --- Υποβολή φόρμας προόδου ---
    @PostMapping
    @Transactional
    public String addProgressRecord(Authentication authentication,
                                    @Valid @ModelAttribute("progressForm") ProgressForm form,
                                    BindingResult result,
                                    Model model) {

        if (authentication == null) return "redirect:/login";

        if (result.hasErrors()) {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email).orElseThrow();
            model.addAttribute("user", user);
            return "progress"; // Επιστροφή στη φόρμα σε περίπτωση λάθους
        }

        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Μετατροπή DTO σε Entity
        ProgressRecord record = new ProgressRecord();
        record.setWeight(form.getWeight());
        record.setRunningTime(form.getRunningTime());
        record.setDate(form.getDate() != null ? form.getDate() : LocalDate.now());
        record.setNotes(form.getNotes());
        record.setUser(user);

        // Ενημέρωση τρεχουσών τιμών χρήστη
        user.setCurrentWeight(form.getWeight());
        user.setRunningTime(form.getRunningTime());
        user.getProgressRecords().add(record);

        userRepository.save(user);

        return "redirect:/profile?success";
    }
}