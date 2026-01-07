package gr.hua.dit.fittrack.web.api;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.service.ProgressService;
import gr.hua.dit.fittrack.core.service.impl.dto.AddProgressRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressRestController {

    private final ProgressService progressService;

    public ProgressRestController(ProgressService progressService) {
        if (progressService == null) throw new NullPointerException();
        this.progressService = progressService;
    }

    @GetMapping("/user/{userId}")
    public List<ProgressRecord> list(@PathVariable Long userId) {
        return progressService.getProgressForUser(userId);
    }

    @PostMapping("/user/{userId}")
    public ProgressRecord add(
            @PathVariable Long userId,
            @Valid @RequestBody AddProgressRequest dto
    ) {
        return progressService.addProgress(userId, dto);
    }
}
