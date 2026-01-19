package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.service.TrainerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(
            final TrainerRepository trainerRepository
    ) {
        if (trainerRepository == null) throw new NullPointerException();

        this.trainerRepository = trainerRepository;
    }

    @Override
    public List<Trainer> findAllTrainers() {
        // Επιστρέφει όλους τους trainers στη βάση
        return trainerRepository.findAll();
    }

    @Override
    public Optional<Trainer> findTrainerById(Long trainerId) {
        // Επιστρέφει έναν trainer με βάση το ID, αν υπάρχει
        return trainerRepository.findById(trainerId);
    }
}