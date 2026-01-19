package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.TrainerNotes;

public record AddTrainerNoteResult(
        boolean created, // true αν η εγγραφή δημιουργήθηκε επιτυχώς
        String reason, // λόγος αποτυχίας (αν υπάρχει)
        TrainerNotes note // η δημιουργημένη TrainerNote (αν υπάρχει)
){
    // Factory method για επιτυχία
    public static AddTrainerNoteResult success(TrainerNotes note) {
        if (note == null) throw new NullPointerException();
        return new AddTrainerNoteResult(true, null, note);
    }
    // Factory method για αποτυχία
    public static AddTrainerNoteResult fail(String reason) {
        if (reason == null) throw new NullPointerException();
        return new AddTrainerNoteResult(false, reason, null);
    }
}