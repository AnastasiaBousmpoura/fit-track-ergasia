package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.service.TrainerNotesService;
import gr.hua.dit.fittrack.web.dto.TrainerNotesForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/trainer/appointments")
public class TrainerNotesController {
    private final TrainerNotesService trainerNotesService;

    public TrainerNotesController(TrainerNotesService trainerNotesService) {
        this.trainerNotesService = trainerNotesService;
    }

    // --- Εμφάνιση σημειώσεων για συγκεκριμένο ραντεβού ---
    @GetMapping("/{appointmentId}/notes")
    public String notes(@PathVariable Long appointmentId, Model model) {
        try {
            model.addAttribute("notes", trainerNotesService.listNotes(appointmentId));
        } catch (Exception e) {
            // Αν αποτύχει το listNotes λόγω auth, βάζουμε μια κενή λίστα για να μην κρασάρει η GET
            model.addAttribute("notes", java.util.Collections.emptyList());
        }

        model.addAttribute("appointmentId", appointmentId);
        model.addAttribute("form", new TrainerNotesForm(""));
        return "appointments-notes";
    }

    // --- Προσθήκη νέας σημείωσης ---
    @PostMapping("/{appointmentId}/notes")
    public String addNote(
            @PathVariable Long appointmentId,
            @ModelAttribute("form") @Valid TrainerNotesForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("appointmentId", appointmentId);
            return "appointments-notes";
        }

        try {
            // Προσπάθεια αποθήκευσης μέσω του Service
            var result = trainerNotesService.addNotes(appointmentId, form.getText());

            if (!result.created()) {
                model.addAttribute("errorMessage", result.reason());
                model.addAttribute("appointmentId", appointmentId);
                model.addAttribute("notes", trainerNotesService.listNotes(appointmentId));
                return "appointments-notes";
            }
        } catch (Exception e) {
            /* ΕΔΩ ΕΙΝΑΙ Η ΛΥΣΗ: Αν το service πετάξει "not authenticated",
               αντί για μαύρη οθόνη, επιστρέφουμε στη σελίδα με μήνυμα σφάλματος.
            */
            model.addAttribute("errorMessage", "Σφάλμα Αυθεντικοποίησης: Ο λογαριασμός σας δεν έχει δικαιώματα Trainer ή το session έληξε.");
            model.addAttribute("appointmentId", appointmentId);
            model.addAttribute("form", form);
            try {
                model.addAttribute("notes", trainerNotesService.listNotes(appointmentId));
            } catch (Exception ex) {
                model.addAttribute("notes", java.util.Collections.emptyList());
            }
            return "appointments-notes";
        }

        return "redirect:/trainer/appointments/" + appointmentId + "/notes";
    }
}