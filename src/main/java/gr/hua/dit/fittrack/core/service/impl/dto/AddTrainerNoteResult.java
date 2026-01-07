package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.TrainerNotes;

public record AddTrainerNoteResult(
        boolean created,
        String reason,
        TrainerNotes note
){
    public static AddTrainerNoteResult success(TrainerNotes note) {
        if (note == null) throw new NullPointerException();
        return new AddTrainerNoteResult(true, null, note);
    }

    public static AddTrainerNoteResult fail(String reason) {
        if (reason == null) throw new NullPointerException();
        return new AddTrainerNoteResult(false, reason, null);
    }
}