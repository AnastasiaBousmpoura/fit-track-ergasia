package gr.hua.dit.fittrack.core.security;

import gr.hua.dit.fittrack.core.model.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class ApplicationUserDetails implements UserDetails {
    private final long personId; // ID χρήστη
    private final String emailAddress; // Email χρήστη
    private final String passwordHash; // Κωδικός
    private final Role role; // Ρόλος

    public ApplicationUserDetails(final long personId,
                                  final String emailAddress,
                                  final String passwordHash,
                                  final Role role) {
        if (personId <= 0) throw new IllegalArgumentException();
        if (emailAddress == null) throw new NullPointerException();
        if (emailAddress.isBlank()) throw new IllegalArgumentException();
        if (passwordHash == null) throw new NullPointerException();
        if (passwordHash.isBlank()) throw new IllegalArgumentException();
        if (role == null) throw new NullPointerException();

        this.personId = personId;
        this.emailAddress = emailAddress;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Επιστρέφει ID
    public long personId() {
        return this.personId;
    }

    // Επιστρέφει ρόλο
    public Role role() {
        return this.role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final String roleName;

        if (this.role == Role.TRAINER) {
            roleName = "ROLE_TRAINER";  // για trainer
        } else if (this.role == Role.USER) {
            roleName = "ROLE_USER";     // για απλό χρήστη/student
        } else {
            throw new RuntimeException("Invalid role: " + this.role);
        }

        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.emailAddress;
    }

    //@Override
    //public boolean isAccountNonExpired() {
    //    return true;
    //}

   // @Override
   // public boolean isAccountNonLocked() {
    //    return true;
    //}

   // @Override
    //public boolean isCredentialsNonExpired() {
      //  return true;
    //}

    @Override
    public boolean isEnabled() {
        return true;
    }
}
