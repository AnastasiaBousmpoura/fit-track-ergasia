package gr.hua.dit.fittrack.core.service.impl.dto;

import gr.hua.dit.fittrack.core.model.entity.Role;

public record UserView(
        Long id, // ID χρήστη
        String email, // Email χρήστη
        String firstName, // Όνομα
        String lastName, // Επώνυμο
        Role role // Ρόλος (USER / TRAINER)
) {

    /**
     * Επιστρέφει πλήρες όνομα χρήστη.
     */
    public String fullName() {
        return firstName + " " + lastName;
    }
}
