package gr.hua.dit.fittrack.core.model.entity;

import jakarta.persistence.*;

@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private String firstName; // Όνομα
    private String lastName; // Επίθετο

    @Column(unique = true)
    private String email; // Email μοναδικό

    private String password; // Κωδικός
    private Role role; // Ρόλος

    private String specialization; // Ειδικότητα
    private String area; // Περιοχή

    // Constructors
    public Trainer() {}


    public Trainer(String firstName, String lastName, String email, String password, String specialization, String area) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.specialization = specialization;
        this.area = area;
        this.role = Role.TRAINER;
    }


    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}