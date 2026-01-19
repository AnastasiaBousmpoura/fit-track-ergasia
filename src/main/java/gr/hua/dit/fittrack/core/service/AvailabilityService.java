package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAvailabilityResult;

import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityService {

    //Δημιουργεί διαθέσιμη ημερομηνία για trainer.
    CreateAvailabilityResult createSlot(Long trainerId, LocalDateTime start, LocalDateTime end);
    //Επιστρέφει όλες τις διαθέσιμες ημερομηνίες ενός trainer.
    List<TrainerAvailability> listSlotsForTrainer(Long trainerId);
    //Διαγράφει διαθέσιμη ημερομηνία για trainer.
    void deleteSlot(Long trainerId, Long slotId);
}