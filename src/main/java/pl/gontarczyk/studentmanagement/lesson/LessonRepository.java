package pl.gontarczyk.studentmanagement.lesson;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pl.gontarczyk.studentmanagement.lesson.model.Lesson;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    boolean existsByTeacherAndDateTimeBetween(Teacher teacher, LocalDateTime from, LocalDateTime to);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Lesson> findWithLockingById(Integer id);
}