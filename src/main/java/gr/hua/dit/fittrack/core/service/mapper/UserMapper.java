package gr.hua.dit.fittrack.core.service.mapper;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.service.impl.dto.UserView;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Μετατρέπει ένα User entity σε UserView DTO.
     * Επιστρέφει null αν ο χρήστης είναι null.
     */
    public UserView convertUserToUserView(User user) {
        if (user == null) {
            return null;
        }

        // Δημιουργία UserView από τα δεδομένα του User
        return new UserView(
                user.getId(), // ID χρήστη
                user.getEmailAddress(), // Email
                user.getUserFirstName(), // Όνομα
                user.getUserLastName(), // Επώνυμο
                user.getRole() // Ρόλος (User/Trainer)
        );
    }
}
