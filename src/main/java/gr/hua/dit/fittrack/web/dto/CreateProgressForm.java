package gr.hua.dit.fittrack.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class CreateProgressForm {

    @NotNull
    @PastOrPresent
    private LocalDate date; // Ημερομηνία καταχώρησης προόδου

    @NotNull
    @Positive
    private Double weight; // Βάρος χρήστη (πρέπει > 0)

    private String notes; // Προαιρετικές σημειώσεις

    // Getters & Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
