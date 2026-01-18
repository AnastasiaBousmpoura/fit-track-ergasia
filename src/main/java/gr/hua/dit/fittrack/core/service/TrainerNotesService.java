package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.TrainerNotes;
import gr.hua.dit.fittrack.core.service.impl.dto.AddTrainerNoteResult;
import java.util.List;

public interface TrainerNotesService {

    AddTrainerNoteResult addNotes(Long appointmentId, String text);

    List<TrainerNotes> listNotes(Long appointmentId);
}
