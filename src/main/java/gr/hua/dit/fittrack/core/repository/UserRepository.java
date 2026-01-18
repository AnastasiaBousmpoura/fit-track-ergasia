package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.Role;
import gr.hua.dit.fittrack.core.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAddressIgnoreCase(String emailAddress);

    Optional<User> findByEmailAddressIgnoreCase(String emailAddress);

    Optional<User> findByEmailAddress(String emailAddress);

    List<User> findByRole(Role role);
}

