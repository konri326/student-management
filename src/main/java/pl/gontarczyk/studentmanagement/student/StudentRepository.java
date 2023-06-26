package pl.gontarczyk.studentmanagement.student;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pl.gontarczyk.studentmanagement.student.model.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByIdAndActiveTrue(Integer id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Student> findWithLockingByIdAndActiveTrue(Integer id);
}