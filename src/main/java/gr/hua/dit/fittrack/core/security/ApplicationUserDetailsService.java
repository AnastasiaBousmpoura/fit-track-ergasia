package gr.hua.dit.fittrack.core.security;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public ApplicationUserDetailsService(UserRepository userRepository) {
        if (userRepository == null) throw new NullPointerException();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isBlank()) throw new IllegalArgumentException();

        // Παίρνουμε τον χρήστη από τη βάση
        User user = userRepository.findByEmailAddressIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        // Δημιουργούμε το UserDetails αντικείμενο
        return new ApplicationUserDetails(
                user.getId(),
                user.getEmailAddress(),
                user.getPassword(),
                user.getRole() // Role.USER ή Role.TRAINER
        );
    }
}
