package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.User;
import java.util.Optional;

public interface UserService {
    //Βρίσκει χρήστη με βάση το email.
    Optional<User> getUserByEmail(String email);
    //Ενημερώνει βάρος και χρόνο τρεξίματος του χρήστη.
    void updateProgress(User user, Double currentWeight, Double runningTime);
}
