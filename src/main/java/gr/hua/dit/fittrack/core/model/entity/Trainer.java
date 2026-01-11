package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;

@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String password; // Απαραίτητο για το login
    private String role;     // Απαραίτητο για το Security Redirect (π.χ. ROLE_TRAINER)

    private String specialization;
    private String area;

    // Default Constructor
    public Trainer() {}

    // Constructor για εύκολη δημιουργία
    public Trainer(String firstName, String lastName, String email, String password, String specialization, String area) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.specialization = specialization;
        this.area = area;
        this.role = "ROLE_TRAINER"; // Default τιμή
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
}