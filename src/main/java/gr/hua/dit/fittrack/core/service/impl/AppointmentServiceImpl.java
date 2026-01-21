package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.*;
import gr.hua.dit.fittrack.core.repository.*;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.WeatherService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final WeatherService weatherService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final TrainerRepository trainerRepository;
    private final TrainerAvailabilityRepository trainerAvailabilityRepository;

    public AppointmentServiceImpl(UserRepository userRepository, AppointmentRepository appointmentRepository,
                                  TrainerRepository trainerRepository, TrainerAvailabilityRepository trainerAvailabilityRepository,
                                  WeatherService weatherService) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.trainerRepository = trainerRepository;
        this.trainerAvailabilityRepository = trainerAvailabilityRepository;
        this.weatherService = weatherService;
    }

    /**
     * Δημιουργία νέου ραντεβού
     */
    @Override
    @Transactional
    public CreateAppointmentResult createAppointment(CreateAppointmentRequest req, boolean notify) {
        // Δε δέχεται παρελθοντικές ημερομηνίες
        if (req.dateTime().isBefore(LocalDateTime.now())) return CreateAppointmentResult.fail("Όχι παρελθοντικές ημερομηνίες.");

        // Κανόνας: Μέγιστο 3 ενεργά μελλοντικά ραντεβού
        long activeCount = appointmentRepository.countByUser_IdAndDateTimeAfterAndStatusNot(req.userId(), LocalDateTime.now(), AppointmentStatus.CANCELLED);
        if (activeCount >= 3) return CreateAppointmentResult.fail("Έχετε φτάσει το όριο των 3 ενεργών ραντεβού.");

        // Έλεγχος διαθεσιμότητας ημέρας (9-9)
        boolean dayAvailable = trainerAvailabilityRepository.existsByTrainer_IdAndAvailableDate(req.trainerId(), req.dateTime().toLocalDate());
        if (!dayAvailable) return CreateAppointmentResult.fail("Ο trainer δεν εργάζεται αυτή τη μέρα.");

        int hour = req.dateTime().getHour();
        if (hour < 9 || hour >= 21) return CreateAppointmentResult.fail("Ώρες λειτουργίας: 09:00 - 21:00.");

        if (appointmentRepository.existsByTrainer_IdAndDateTime(req.trainerId(), req.dateTime())) {
            return CreateAppointmentResult.fail("Το slot είναι ήδη πιασμένο.");
        }

        // Φόρτωση χρήστη & trainer
        User user = userRepository.findById(req.userId()).orElse(null);
        Trainer trainer = trainerRepository.findById(req.trainerId()).orElse(null);
        if (user == null || trainer == null) return CreateAppointmentResult.fail("Χρήστης/Trainer δεν βρέθηκε.");

        // Δημιουργία appointment
        Appointment appt = new Appointment(user, trainer, req.dateTime(), req.type(), req.notes(), "Athens,GR", null);
        appt.setStatus(AppointmentStatus.PENDING);

        // Καιρός μόνο για outdoor
        if (req.type() == AppointmentType.OUTDOOR) {
            try {
                var weather = weatherService.getWeatherFor(req.dateTime(), "Athens,GR");

                if (weather != null && weather.getSummary() != null && !weather.getSummary().isBlank()) {
                    appt.setWeatherSummary(weather.getSummary());
                } else {
                    appt.setWeatherSummary("Weather N/A");
                }

            } catch (Exception e) {
                appt.setWeatherSummary("Weather N/A");
                e.printStackTrace();
            }
        }


        return CreateAppointmentResult.success(appointmentRepository.save(appt));
    }

    /**
     * Επιστρέφει όλες τις διαθέσιμες μελλοντικές ημερομηνίες trainer.
     */
    @Override
    public List<LocalDateTime> getAvailableSlots(Long trainerId) {
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        trainerAvailabilityRepository.findByTrainer_Id(trainerId).forEach(av -> {
            for (int h = 9; h < 21; h++) {
                LocalDateTime s = LocalDateTime.of(av.getAvailableDate(), LocalTime.of(h, 0));
                if (s.isAfter(now)) slots.add(s);
            }
        });

        // Αφαίρεση ήδη κλεισμένων ραντεβού trainer
        List<LocalDateTime> booked = appointmentRepository.findByTrainer_Id(trainerId).stream()
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
                .map(Appointment::getDateTime).toList();

        slots.removeAll(booked);
        return slots.stream().sorted().collect(Collectors.toList());
    }

    // Getters / Updaters
    @Override public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override public List<Appointment> getAppointmentsForTrainer(String email) {
        return appointmentRepository.findByTrainer_Email(email);
    }

    @Override public List<Appointment> getAppointmentsByUser(String email) {
        return appointmentRepository.findByUser_EmailAddress(email);
    }

    @Override public List<Appointment> getAppointmentsByTrainer(String email) {
        return appointmentRepository.findByTrainer_Email(email);
    }

    @Override @Transactional public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
    @Override @Transactional public void updateStatus(Long id, String status) {
        appointmentRepository.findById(id).ifPresent(a -> a.setStatus(AppointmentStatus.valueOf(status.toUpperCase())));
    }

    @Override @Transactional public void updateNotes(Long id, String notes) {
        appointmentRepository.findById(id).ifPresent(a -> a.setNotes(notes));
    }

    @Override @Transactional public void cancelAppointment(Long id) {
        appointmentRepository.findById(id).ifPresent(a -> a.setStatus(AppointmentStatus.CANCELLED));
    }

    @Override @Transactional public void setTrainerAvailability(Long tId, LocalDateTime s, LocalDateTime e) {
        /* Placeholder */
    }
}