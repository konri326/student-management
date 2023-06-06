package pl.gontarczyk.studentmanagement.teacher;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher save(Teacher teacher) {
        teacher.setActive(true);
        return teacherRepository.save(teacher);
    }

    public Teacher findById(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found!"));
    }

    public Teacher findWithLockingById(int id) {
        return teacherRepository.findWithLockingById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found!"));
    }

    @Transactional
    @Modifying
    public void delete(int id) {
        Teacher teacher = findWithLockingById(id);
        teacher.setActive(false);
        teacherRepository.save(teacher);
    }
}