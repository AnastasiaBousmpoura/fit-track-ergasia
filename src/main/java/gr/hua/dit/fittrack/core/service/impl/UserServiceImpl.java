package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.UserService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        if(userRepository == null) throw new NullPointerException();

        this.userRepository = userRepository;
    }

    // Επιστρέφει τον χρήστη με βάση το email, αν υπάρχει
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAddress(email);
    }

    @Override
    public void updateProgress(User user, Double currentWeight, Double runningTime) {
        // Ενημερώνει τα πεδία progress του χρήστη και αποθηκεύει τις αλλαγές
        user.setCurrentWeight(currentWeight);
        user.setRunningTime(runningTime);
        userRepository.save(user);
    }

}