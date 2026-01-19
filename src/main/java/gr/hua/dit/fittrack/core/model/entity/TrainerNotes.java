package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;

@Entity
public class TrainerNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id; // PK

    @ManyToOne
    private Appointment appointment; // Σχετικό ραντεβού

    @ManyToOne
    private Trainer trainer; // Trainer που έγραψε τη σημείωση

    @Column(name = "text")
    private String text; // Κείμενο σημείωσης

    // Constructor
    public TrainerNotes() {
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
}
