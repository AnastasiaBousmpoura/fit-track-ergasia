package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TrainerAvailabilityRepository extends JpaRepository<TrainerAvailability, Long> {

    List<TrainerAvailability> findByTrainer_Id(Long trainerId); // Διαθεσιμότητες συγκεκριμένου trainer
    boolean existsByTrainer_IdAndAvailableDate(Long trainerId, LocalDate availableDate); // Έλεγχος αν υπάρχει διαθέσιμη μέρα
}