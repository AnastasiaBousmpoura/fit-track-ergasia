package gr.hua.dit.fittrack.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ProgressForm {

    @NotNull
    private LocalDate date;

    @NotNull
    @Positive
    private Double weight;

    @Size(max = 500)
    private String notes;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
