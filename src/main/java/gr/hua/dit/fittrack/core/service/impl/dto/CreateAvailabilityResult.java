package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;

public record CreateAvailabilityResult(
        boolean created, // αν δημιουργήθηκε επιτυχώς
        String reason, // λόγος αποτυχίας (αν υπάρχει)
        TrainerAvailability slot // το availability slot (αν υπάρχει)
) {

    // Επιστρέφει αποτέλεσμα επιτυχίας
    public static CreateAvailabilityResult success(final TrainerAvailability slot) {
        if (slot == null) throw new NullPointerException();
        return new CreateAvailabilityResult(true, null, slot);
    }

    // Επιστρέφει αποτέλεσμα αποτυχίας
    public static CreateAvailabilityResult fail(final String reason) {
        if (reason == null) throw new NullPointerException();
        return new CreateAvailabilityResult(false, reason, null);
    }
}