package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 1. Για τον Χρήστη (Πελάτη)
    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByUser_EmailAddress(String email);

    // 2. Για τον Trainer
    List<Appointment> findByTrainerId(Long trainerId);

    // Αυτή η μέθοδο χρησιμοποιείται από τον Service για τη λίστα του Trainer
    List<Appointment> findByTrainer_Email(String email);

    // 3. Έλεγχοι Logic (Validation)
    // Προσοχή: Χωρίς underscores ανάμεσα στο όνομα της οντότητας και το Id
    boolean existsByTrainerIdAndDateTime(Long trainerId, LocalDateTime dateTime);
    boolean existsByUserIdAndDateTime(Long userId, LocalDateTime dateTime);
}