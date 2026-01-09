package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import gr.hua.dit.fittrack.core.repository.AppointmentRepository;
import gr.hua.dit.fittrack.core.repository.TrainerAvailabilityRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final TrainerRepository trainerRepository;
    private final TrainerAvailabilityRepository trainerAvailabilityRepository;

    public AppointmentServiceImpl(
            UserRepository userRepository,
            AppointmentRepository appointmentRepository,
            TrainerRepository trainerRepository,
            TrainerAvailabilityRepository trainerAvailabilityRepository
    ) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.trainerRepository = trainerRepository;
        this.trainerAvailabilityRepository = trainerAvailabilityRepository;
    }

    // ---------------------------------------------------
    // 1. Δημιουργία Ραντεβού (POST)
    // ---------------------------------------------------
    @Override
    @Transactional
    public CreateAppointmentResult createAppointment(CreateAppointmentRequest req, boolean notify) {

        // Έλεγχος αν η ημερομηνία είναι στο παρελθόν
        if (req.dateTime().isBefore(LocalDateTime.now())) {
            return CreateAppointmentResult.fail("Δεν επιτρέπονται ραντεβού στο παρελθόν.");
        }

        // Έλεγχος αν ο trainer είναι διαθέσιμος βάσει του ωραρίου του (Availability)
        boolean hasAvailability = trainerAvailabilityRepository
                .existsByTrainer_IdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        req.trainerId(), req.dateTime(), req.dateTime()
                );

        if (!hasAvailability) {
            return CreateAppointmentResult.fail("Ο trainer δεν είναι διαθέσιμος αυτή την ώρα.");
        }

        // Έλεγχος αν ο trainer έχει ήδη άλλο ραντεβού εκείνη την ώρα (Busy)
        boolean trainerBusy = appointmentRepository.existsByTrainer_IdAndDateTime(req.trainerId(), req.dateTime());
        if (trainerBusy) {
            return CreateAppointmentResult.fail("Υπάρχει ήδη ραντεβού του trainer την ίδια ώρα.");
        }

        // Φόρτωση User & Trainer
        User user = userRepository.findById(req.userId()).orElse(null);
        Trainer trainer = trainerRepository.findById(req.trainerId()).orElse(null);

        if (user == null || trainer == null) {
            return CreateAppointmentResult.fail("Δεν βρέθηκε ο χρήστης ή ο trainer.");
        }

        // Δημιουργία και Αποθήκευση
        Appointment appt = new Appointment();
        appt.setUser(user);
        appt.setTrainer(trainer);
        appt.setDateTime(req.dateTime());
        appt.setType(req.type());
        appt.setNotes(req.notes());

        Appointment saved = appointmentRepository.save(appt);
        return CreateAppointmentResult.success(saved);
    }

    // ---------------------------------------------------
    // 2. Διαγραφή Ραντεβού
    // ---------------------------------------------------
    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Το ραντεβού δεν βρέθηκε.");
        }
        appointmentRepository.deleteById(id);
    }

    // ---------------------------------------------------
    // 3. Λήψη Διαθέσιμων Slots (GET για το Dropdown)
    // ---------------------------------------------------
    @Override
    public List<LocalDateTime> getAvailableSlots(Long trainerId) {
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // α) Φέρνουμε τα ωράρια του trainer από τη βάση
        List<TrainerAvailability> availabilityList = trainerAvailabilityRepository.findByTrainer_Id(trainerId);

        for (TrainerAvailability av : availabilityList) {
            LocalDateTime start = av.getStartTime();
            LocalDateTime end = av.getEndTime();

            // Παράγουμε slots ανά 1 ώρα
            LocalDateTime tempSlot = start;
            while (tempSlot.isBefore(end)) {
                // Προσθέτουμε μόνο αν η ώρα είναι μελλοντική
                if (tempSlot.isAfter(now)) {
                    slots.add(tempSlot);
                }
                tempSlot = tempSlot.plusHours(1);
            }
        }

        // β) Φέρνουμε τα ήδη κλεισμένα ραντεβού του trainer
        List<LocalDateTime> bookedTimes = appointmentRepository.findByTrainer_Id(trainerId)
                .stream()
                .map(Appointment::getDateTime)
                .toList();

        // γ) Αφαιρούμε τα κλεισμένα από τη λίστα των slots
        slots.removeAll(bookedTimes);

        // δ) Επιστροφή ταξινομημένης λίστας
        return slots.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void setTrainerAvailability(Long trainerId, LocalDateTime start, LocalDateTime end) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        TrainerAvailability availability = new TrainerAvailability();
        availability.setTrainer(trainer);
        availability.setStartTime(start);
        availability.setEndTime(end);

        trainerAvailabilityRepository.save(availability);
    }
}