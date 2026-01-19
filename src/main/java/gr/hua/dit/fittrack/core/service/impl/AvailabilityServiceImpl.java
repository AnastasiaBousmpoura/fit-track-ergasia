package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import gr.hua.dit.fittrack.core.repository.TrainerAvailabilityRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.service.AvailabilityService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAvailabilityResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
    private final TrainerAvailabilityRepository availabilityRepository;
    private final TrainerRepository trainerRepository;

    public AvailabilityServiceImpl(TrainerAvailabilityRepository availabilityRepository, TrainerRepository trainerRepository) {
        this.availabilityRepository = availabilityRepository;
        this.trainerRepository = trainerRepository;
    }

    /**
     * Δημιουργεί διαθεσιμότητα trainer για συγκεκριμένη ημερομηνία.
     */
    @Transactional
    @Override
    public CreateAvailabilityResult createSlot(Long trainerId, LocalDateTime start, LocalDateTime end) {
        // Δεν επιτρέπονται παρελθοντικές ημερομηνίες
        LocalDate date = start.toLocalDate();
        if (date.isBefore(LocalDate.now())) return CreateAvailabilityResult.fail("Invalid date");

        // Έλεγχος trainer
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer == null) return CreateAvailabilityResult.fail("Trainer not found");

        if (availabilityRepository.existsByTrainer_IdAndAvailableDate(trainerId, date)) {
            return CreateAvailabilityResult.fail("Availability already set for this date");
        }

        TrainerAvailability slot = new TrainerAvailability();
        slot.setTrainer(trainer);
        slot.setAvailableDate(date);
        return CreateAvailabilityResult.success(availabilityRepository.save(slot));
    }

    /**
     * Επιστρέφει όλες τις διαθέσιμες ημερομηνίες ενός trainer.
     */
    @Override
    public List<TrainerAvailability> listSlotsForTrainer(Long trainerId) {
        return availabilityRepository.findByTrainer_Id(trainerId);
    }

    /**
     * Διαγράφει διαθέσιμη ημερομηνία αν ανήκει στον trainer.
     */
    @Transactional
    @Override
    public void deleteSlot(Long trainerId, Long slotId) {
        availabilityRepository.findById(slotId).ifPresent(slot -> {
            if (slot.getTrainer().getId().equals(trainerId)) availabilityRepository.delete(slot);
        });
    }
}