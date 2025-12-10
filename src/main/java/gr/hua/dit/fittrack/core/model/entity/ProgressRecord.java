package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class ProgressRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name= "id")
    private Long id;

    @ManyToOne
    private User user;

    @Column
    private LocalDate date;

    @Column
    private double weight;

    @Column
    private String notes;

    //constructor
    public ProgressRecord() {
    }

    //Getters and Setters
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
