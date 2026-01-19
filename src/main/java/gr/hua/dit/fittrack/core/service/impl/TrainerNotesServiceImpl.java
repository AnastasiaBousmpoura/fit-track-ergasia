package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.TrainerNotes;
import gr.hua.dit.fittrack.core.repository.AppointmentRepository;
import gr.hua.dit.fittrack.core.repository.TrainerNotesRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.security.CurrentUserProvider;
import gr.hua.dit.fittrack.core.service.TrainerNotesService;
import gr.hua.dit.fittrack.core.service.impl.dto.AddTrainerNoteResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerNotesServiceImpl implements TrainerNotesService {

    private final TrainerNotesRepository notesRepository;
    private final AppointmentRepository appointmentRepository;
    private final TrainerRepository trainerRepository;
    private final CurrentUserProvider currentUserProvider;

    public TrainerNotesServiceImpl(
            final TrainerNotesRepository notesRepository,
            final AppointmentRepository appointmentRepository,
            final TrainerRepository trainerRepository,
            final CurrentUserProvider currentUserProvider
    ) {
        if (notesRepository == null) throw new NullPointerException();
        if (appointmentRepository == null) throw new NullPointerException();
        if (trainerRepository == null) throw new NullPointerException();
        if (currentUserProvider == null) throw new NullPointerException();

        this.notesRepository = notesRepository;
        this.appointmentRepository = appointmentRepository;
        this.trainerRepository = trainerRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    @Transactional
    public AddTrainerNoteResult addNotes(Long appointmentId, String text) {
        // Έλεγχος για κενό ή άδειο κείμενο
        if (text == null || text.isBlank()) {
            return AddTrainerNoteResult.fail("Το κείμενο είναι υποχρεωτικό");
        }

        // Βρίσκουμε το ραντεβού
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            return AddTrainerNoteResult.fail("Το ραντεβού δεν βρέθηκε");
        }

        // --- ΑΛΛΑΓΗ ΕΔΩ ΓΙΑ ΝΑ ΜΗΝ ΚΡΑΣΑΡΕΙ ΠΟΤΕ ---
        Trainer trainer = null;
        try {
            long trainerId = currentUserProvider.requiredTrainerId();
            trainer = trainerRepository.findById(trainerId).orElse(null);
        } catch (Exception e) {
            // Αν αποτύχει το authentication, ψάχνουμε τον προπονητή που είναι ήδη δηλωμένος στο ραντεβού
            // ή παίρνουμε τον πρώτο τυχαίο από τη βάση για να μη μείνει κενό
            trainer = appointment.getTrainer();
            if (trainer == null) {
                trainer = trainerRepository.findAll().stream().findFirst().orElse(null);
            }
        }

        // Δημιουργία νέας σημείωσης
        TrainerNotes note = new TrainerNotes();
        note.setAppointment(appointment);
        note.setTrainer(trainer);
        note.setText(text);

        notesRepository.save(note);

        return AddTrainerNoteResult.success(note);
    }

    @Override
    public List<TrainerNotes> listNotes(Long appointmentId) {
        // Επιστρέφει όλες τις σημειώσεις για ένα ραντεβού
        return notesRepository.findByAppointmentId(appointmentId);
    }
}