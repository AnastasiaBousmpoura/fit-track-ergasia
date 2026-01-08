package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.service.impl.dto.AddProgressRequest;

import java.util.List;

public interface ProgressService {

    ProgressRecord addProgress(Long userId, AddProgressRequest dto);

    List<ProgressRecord> getProgressForUser(Long userId);
}
