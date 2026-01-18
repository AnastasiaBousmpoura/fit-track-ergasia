package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.User;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserByEmail(String email);

    void updateProgress(User user, Double currentWeight, Double runningTime);
}
