package pl.gontarczyk.studentmanagement.student;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gontarczyk.studentmanagement.exception.IncorrectConnectionOfObjectsException;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.teacher.TeacherService;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherService teacherService;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student save(Student student, int teacherId) {
        Teacher teacher = teacherService.findByIdAndActive(teacherId);
        if (!teacher.getLanguages().contains(student.getLanguage())) {
            throw new IncorrectConnectionOfObjectsException("Teacher does not know this language!");
        }
        student.setTeacher(teacherService.findById(teacherId));
        student.setActive(true);
        return studentRepository.save(student);
    }

    public Student findById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found!"));
    }

    public Student findWithLockingById(int id) {
        return studentRepository.findWithLockingById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found!"));
    }

    @Transactional
    @Modifying
    public void delete(int id) {
        Student student = findWithLockingById(id);
        student.setActive(false);
        studentRepository.save(student);
    }
}