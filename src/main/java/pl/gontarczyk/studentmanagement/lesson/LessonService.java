package pl.gontarczyk.studentmanagement.lesson;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gontarczyk.studentmanagement.lesson.model.Lesson;
import pl.gontarczyk.studentmanagement.student.StudentService;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.teacher.TeacherService;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Transactional
    public Lesson save(Lesson lesson, int studentId) {
        Student student = studentService.findWithLockingById(studentId);
        Teacher teacher = teacherService.findWithLockingById(student.getTeacher().getId());
        if (!isOverlap(lesson.getDateTime(), teacher)) {
            lesson.setStudent(student);
            lesson.setTeacher(teacher);
        }
        return lessonRepository.save(lesson);
    }

    @Transactional
    @Modifying
    public Lesson update(Lesson newLesson, int id) {
        Lesson lesson = findWithLockingById(id);
        if (!isOverlap(newLesson.getDateTime(), lesson.getTeacher())) {
            lesson.setDateTime(newLesson.getDateTime());
        }
        return lessonRepository.save(lesson);
    }

    public Lesson findById(int id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found!"));
    }

    public Lesson findWithLockingById(int id) {
        return lessonRepository.findWithLockingById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found!"));
    }

    public void delete(int id) {
        lessonRepository.deleteById(id);
    }

    private boolean isOverlap(LocalDateTime dateTime, Teacher teacher) {
        if (existsByTeacherAndStartOrEndDateTime(teacher, dateTime.minusMinutes(59), dateTime.plusMinutes(59))) {
            throw new EntityExistsException("Lesson is overlap with another!");
        }
        return false;
    }

    private boolean existsByTeacherAndStartOrEndDateTime(Teacher teacher, LocalDateTime from, LocalDateTime to) {
        return lessonRepository.existsByTeacherAndDateTimeBetween(teacher, from, to);
    }
}