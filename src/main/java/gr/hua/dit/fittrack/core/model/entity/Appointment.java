package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User userId;

    @ManyToOne
    private Trainer trainerId;

    private LocalDateTime dateTime;

    private String type; // π.χ. "Cardio", "Strength"

    // getters & setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUserId() { return userId; }
    public void setUserId(User userId) { this.userId = userId; }

    public Trainer getTrainerId() { return trainerId; }
    public void setTrainerId(Trainer trainerId) { this.trainerId = trainerId; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
