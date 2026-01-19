package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.repository.TrainerRepository; // Προσοχή στο σωστό πακέτο σου
import gr.hua.dit.fittrack.core.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Αυτό λείπει για το Model
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trainers")
public class TrainerViewController {

    private final TrainerService trainerService;

    // Το Spring θα κάνει αυτόματα inject το bean του Repository
    public TrainerViewController(final TrainerService trainerService) {
        if (trainerService == null) throw new NullPointerException();

        this.trainerService = trainerService;
    }

    // ---Λίστα όλων των trainers ---
    @GetMapping
    public String showPublicTrainers(Model model) {
        // Τραβάμε όλους τους trainers για να τους δει ο Επισκέπτης [cite: 184]
        model.addAttribute("trainers", trainerService.findAllTrainers());
        return "trainers-list";
    }
}