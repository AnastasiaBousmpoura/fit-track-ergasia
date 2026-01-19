package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "appointments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_appointments_trainer_datetime",
                        columnNames = {"trainer_id", "date_time"}
                )
        }
)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Χρήστης

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer; // Trainer

    @Column(nullable = false)
    private LocalDateTime dateTime; // Ημερομηνία & ώρα ραντεβού



    @Column(length = 80)
    private String location; // Τοποθεσία

    @Column(length = 255)
    private String weatherSummary; // Περιγραφή καιρού

    @Column(length = 2000)
    private String notes; // Σημειώσεις

    @Enumerated(EnumType.STRING)
    private AppointmentType type; // Τύπος ραντεβού

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // Κατάσταση ραντεβού

    public Appointment() {
    }

    public Appointment(User user, Trainer trainer, LocalDateTime dateTime, AppointmentType type, String notes, String location, String weatherSummary) {
        this.user = user;
        this.trainer = trainer;
        this.dateTime = dateTime;
        this.type = type;
        this.notes = notes;
        this.location = location;
        this.weatherSummary = weatherSummary;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeatherSummary() {
        return weatherSummary;
    }

    public void setWeatherSummary(String weatherSummary) {
        this.weatherSummary = weatherSummary;
    }

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}