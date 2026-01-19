package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.Trainer;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    //Επιστρέφει όλους τους registered trainers.
    List<Trainer> findAllTrainers();
    //Επιστρέφει trainer με βάση το ID.
    Optional<Trainer> findTrainerById(Long trainerId);
}