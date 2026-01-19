package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import gr.hua.dit.fittrack.core.repository.TrainerAvailabilityRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/trainer/availability")
public class TrainerAvailabilityController {

    private final AppointmentService appointmentService;
    private final TrainerRepository trainerRepository;
    private final TrainerAvailabilityRepository availabilityRepository;

    public TrainerAvailabilityController(AppointmentService appointmentService,
                                         TrainerRepository trainerRepository,
                                         TrainerAvailabilityRepository availabilityRepository) {
        this.appointmentService = appointmentService;
        this.trainerRepository = trainerRepository;
        this.availabilityRepository = availabilityRepository;
    }

    // --- Εμφάνιση φόρμας διαθεσιμότητας ---
    @GetMapping
    public String showAvailabilityForm(Authentication authentication, Model model) {
        String email = authentication.getName();
        Trainer trainer = trainerRepository.findByEmail(email).orElseThrow();

        // Φέρνουμε όλες τις ημέρες που έχει ήδη δηλώσει ο Trainer
        List<TrainerAvailability> avails = availabilityRepository.findByTrainer_Id(trainer.getId());
        model.addAttribute("availabilities", avails);

        return "trainer-availability"; // Το HTML που φτιάξαμε πριν
    }

    // --- Αποθήκευση νέας ημερομηνίας διαθεσιμότητας ---
    @PostMapping("/save")
    public String saveAvailability(@RequestParam("availableDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   Authentication authentication) {

        String email = authentication.getName();
        Trainer trainer = trainerRepository.findByEmail(email).orElseThrow();

        // Έλεγχος αν υπάρχει ήδη η ημερομηνία για να μην έχουμε διπλότυπα
        boolean exists = availabilityRepository.existsByTrainer_IdAndAvailableDate(trainer.getId(), date);

        if (!exists) {
            TrainerAvailability availability = new TrainerAvailability();
            availability.setTrainer(trainer);
            availability.setAvailableDate(date);
            availabilityRepository.save(availability);
        }

        // Redirect πίσω στη σελίδα του προγράμματος (ή στο /appointments/my-appointments αν θες)
        return "redirect:/appointments/my-appointments";
    }

    // --- Διαγραφή υπάρχουσας διαθεσιμότητας ---
    @PostMapping("/delete/{id}")
    public String deleteAvailability(@PathVariable Long id) {
        availabilityRepository.deleteById(id);
        return "redirect:/trainer/availability";
    }
}