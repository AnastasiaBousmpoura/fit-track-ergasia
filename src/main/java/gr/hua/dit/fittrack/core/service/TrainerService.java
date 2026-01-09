package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {

    List<Trainer> findAllTrainers();

    Optional<Trainer> findTrainerById(Long trainerId);
}