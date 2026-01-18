package gr.hua.dit.fittrack.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ProgressForm {

    @NotNull(message = "Η ημερομηνία είναι υποχρεωτική")
    private LocalDate date = LocalDate.now();

    @NotNull(message = "Το βάρος είναι υποχρεωτικό")
    @Positive(message = "Το βάρος πρέπει να είναι μεγαλύτερο από μηδέν (0)")
    private Double weight;

    @NotNull(message = "Ο χρόνος τρεξίματος είναι υποχρεωτικός")
    @PositiveOrZero(message = "Ο χρόνος τρεξίματος δεν μπορεί να είναι αρνητικός")
    private Double runningTime;

    @Size(max = 500, message = "Οι σημειώσεις δεν μπορούν να υπερβαίνουν τους 500 χαρακτήρες")
    private String notes;

    // Getters & Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getRunningTime() { return runningTime; }
    public void setRunningTime(Double runningTime) { this.runningTime = runningTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}