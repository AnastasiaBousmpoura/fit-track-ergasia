package gr.hua.dit.fittrack.core.security;

import gr.hua.dit.fittrack.core.model.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class CurrentUserProvider {

    /**
     * Επιστρέφει τον τρέχοντα user, αν υπάρχει.
     */
    public Optional<CurrentUser> getCurrentUser () {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof ApplicationUserDetails userDetails) {
            return Optional.of(new CurrentUser(userDetails.personId(), userDetails.getUsername(), userDetails.role()));
        }
        return Optional.empty();
    }

    /**
     * Επιστρέφει τον τρέχοντα χρήστη ή πετάει exception αν δεν υπάρχει
     */
    public CurrentUser requireCurrentUser () {
        return this.getCurrentUser().orElseThrow(() -> new SecurityException("not authenticated"));
    }

    /**
     * Επιστρέφει το ID του χρήστη μόνο αν είναι TRAINER, αλλιώς exception
     */
    public long requiredTrainerId () {
        final var currentUser = this.requireCurrentUser();
        if (currentUser.role() != Role.TRAINER) throw new SecurityException("Trainer type/role required");
        return currentUser.id();
    }
}