package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerAvailabilityRepository extends JpaRepository<TrainerAvailability, Long> {

    List<TrainerAvailability> findByTrainerId(Long trainerId);
}

// ekremmothta na elegxei ama o trainer einei hdh se ranteboy