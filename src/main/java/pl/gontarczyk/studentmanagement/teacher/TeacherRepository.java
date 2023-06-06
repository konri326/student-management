package pl.gontarczyk.studentmanagement.teacher;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Teacher> findWithLockingById(Integer id);
}