package gr.hua.dit.fittrack.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "progress_records")
public class ProgressRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; // Χρήστης

    @Column(nullable = false)
    private LocalDate date; // Ημερομηνία καταγραφής

    @Column(nullable = false)
    private double weight; // Βάρος

    @Column(name = "running_time")
    private double runningTime; // Χρόνος τρεξίματος

    @Column(length = 500)
    private String notes; // Σημειώσεις

    // Constructors
    public ProgressRecord() {
        this.date = LocalDate.now();
    }

    public ProgressRecord(User user, double weight, double runningTime, String notes) {
        this.user = user;
        this.weight = weight;
        this.runningTime = runningTime;
        this.notes = notes;
        this.date = LocalDate.now();
    }

    // Getters and Setters
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(double runningTime) {
        this.runningTime = runningTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}