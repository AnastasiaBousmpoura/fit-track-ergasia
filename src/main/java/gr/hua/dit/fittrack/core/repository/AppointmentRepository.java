package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUser_Id(Long userId);
    List<Appointment> findByUser_EmailAddress(String email);
    List<Appointment> findByTrainer_Id(Long trainerId);
    List<Appointment> findByTrainer_Email(String email);

    boolean existsByTrainer_IdAndDateTime(Long trainerId, LocalDateTime dateTime);
    boolean existsByUser_IdAndDateTime(Long userId, LocalDateTime dateTime);


    long countByUser_IdAndDateTimeAfterAndStatusNot(Long userId, LocalDateTime dateTime, gr.hua.dit.fittrack.core.model.entity.AppointmentStatus status);
}