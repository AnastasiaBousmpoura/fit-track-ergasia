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

    public TrainerNotesController(
            TrainerNotesService trainerNotesService
    ) {
        if (trainerNotesService == null) throw new NullPointerException();

        this.trainerNotesService = trainerNotesService;
    }

    @GetMapping("/{appointmentId}/notes")
    public String notes(
            @PathVariable Long appointmentId,
            Model model
    ) {
        model.addAttribute("notes",
                trainerNotesService.listNotes(appointmentId));
        model.addAttribute("appointmentId", appointmentId);
        model.addAttribute("form", new TrainerNotesForm(""));
        return "trainer_notes";
    }

    @PostMapping("/{appointmentId}/notes")
    public String addNote(
            @PathVariable Long appointmentId,
            @ModelAttribute("form") @Valid TrainerNotesForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "trainer_notes";
        }

        var result = trainerNotesService.addNotes(appointmentId, form.getText());

        if (!result.created()) {
            model.addAttribute("errorMessage", result.reason());
            return "trainer_notes";
        }

        return "redirect:/trainer/appointments/" + appointmentId + "/notes";
    }
}