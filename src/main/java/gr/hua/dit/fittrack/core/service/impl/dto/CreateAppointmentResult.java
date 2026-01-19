package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.Appointment;

public record CreateAppointmentResult (
    boolean created, // true αν το ραντεβού δημιουργήθηκε επιτυχώς
    String reason, // λόγος αποτυχίας (αν υπάρχει)
    Appointment appointment // η δημιουργημένη εγγραφή Appointment (αν υπάρχει)
) {
    // Δημιουργεί αντικείμενο αποτελέσματος με επιτυχία
        public static CreateAppointmentResult success(final Appointment appointment) {
            if (appointment == null) throw new NullPointerException();
            return new CreateAppointmentResult(true, null, appointment);
        }

    // Δημιουργεί αντικείμενο αποτελέσματος με αποτυχία
        public static CreateAppointmentResult fail(final String reason) {
            if (reason == null) throw new NullPointerException();
            if (reason.isBlank()) throw new IllegalArgumentException();
            return new CreateAppointmentResult(false, reason, null);
        }
}
