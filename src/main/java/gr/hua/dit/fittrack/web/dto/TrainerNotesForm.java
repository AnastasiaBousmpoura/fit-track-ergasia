package gr.hua.dit.fittrack.web.dto;

import jakarta.validation.constraints.NotBlank;

public class TrainerNotesForm {
    private String text; // Κείμενο σημείωσης του Trainer

    public TrainerNotesForm() {} // default constructor για Spring

    // Getter & Setter
    public TrainerNotesForm(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}