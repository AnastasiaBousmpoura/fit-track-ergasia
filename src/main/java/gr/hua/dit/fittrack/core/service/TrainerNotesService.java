package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.TrainerNotes;
import gr.hua.dit.fittrack.core.service.impl.dto.AddTrainerNoteResult;
import java.util.List;

public interface TrainerNotesService {
    //Προσθέτει σημείωση για συγκεκριμένο ραντεβού.
    AddTrainerNoteResult addNotes(Long appointmentId, String text);
    //Επιστρέφει όλες τις σημειώσεις για ένα ραντεβού.
    List<TrainerNotes> listNotes(Long appointmentId);
}
