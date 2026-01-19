package gr.hua.dit.fittrack.web.rest;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.service.impl.ProgressServiceImpl;
import gr.hua.dit.fittrack.core.service.impl.dto.AddProgressRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressRestController {

    private final ProgressServiceImpl progressService;

    public ProgressRestController(ProgressServiceImpl progressService) {
        if (progressService == null) throw new NullPointerException();
        this.progressService = progressService;
    }

    // --- Λίστα όλων των progress records για έναν χρήστη ---
    // GET /api/progress/user/{userId}
    @GetMapping("/user/{userId}")
    public List<ProgressRecord> list(@PathVariable Long userId) {
        return progressService.getProgressForUser(userId);
    }

    // --- Προσθήκη νέου progress record για έναν χρήστη ---
    // POST /api/progress/user/{userId}
    @PostMapping("/user/{userId}")
    public ProgressRecord add(
            @PathVariable Long userId,
            @Valid @RequestBody AddProgressRequest dto
    ) {
        return progressService.addProgress(userId, dto);
    }
}
