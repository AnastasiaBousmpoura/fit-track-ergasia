package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.service.ProgressService;
import gr.hua.dit.fittrack.core.service.impl.ProgressServiceImpl;
import gr.hua.dit.fittrack.core.service.impl.dto.AddProgressRequest;
import gr.hua.dit.fittrack.web.dto.CreateProgressForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/{userId}/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    // 1) Λίστα ιστορικού
    @GetMapping
    public String list(@PathVariable Long userId, Model model) {
        List<ProgressRecord> records = progressService.getProgressForUser(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("records", records);
        return "progress/list";
    }

    // 2) Φόρμα νέας μέτρησης
    @GetMapping("/new")
    public String newForm(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("form", new CreateProgressForm());
        return "progress/new";
    }

    // 3) Submit φόρμας
    @PostMapping
    public String create(
            @PathVariable Long userId,
            @Valid @ModelAttribute("form") CreateProgressForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", userId);
            return "progress/new";
        }

        AddProgressRequest dto = new AddProgressRequest(
                form.getDate(),
                form.getWeight(),
                form.getNotes()
        );

        progressService.addProgress(userId, dto);

        return "redirect:/users/" + userId + "/progress";
    }

}
