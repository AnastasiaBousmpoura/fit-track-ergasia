package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.service.impl.dto.AddProgressRequest;

import java.util.List;

public interface ProgressService {
    //Προσθέτει νέο progress record για χρήστη.
    ProgressRecord addProgress(Long userId, AddProgressRequest dto);
    //Επιστρέφει όλα τα progress records ενός χρήστη.
    List<ProgressRecord> getProgressForUser(Long userId);
}
