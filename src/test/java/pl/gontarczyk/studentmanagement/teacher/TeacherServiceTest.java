package pl.gontarczyk.studentmanagement.teacher;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void testFindAll_shouldReturnListWithAllTeachers() {
        List<Teacher> teachers = createListWithTeachers();
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> returnedTeachers = teacherService.findAll();

        assertEquals(returnedTeachers.size(), teachers.size());

        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testSave_shouldSaveCorrectTeacherInDatabase() {
        Teacher teacher = createTeacher();
        int numberOfLanguages = teacher.getLanguages().size();
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher returnedTeacher = teacherService.save(teacher);

        assertEquals(returnedTeacher.getId(), teacher.getId());
        assertEquals(returnedTeacher.getFirstName(), teacher.getFirstName());
        assertEquals(returnedTeacher.getLastName(), teacher.getLastName());
        assertEquals(returnedTeacher.getLanguages().size(), numberOfLanguages);

        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void testFindById_shouldReturnTeacherFromDatabaseById() {
        Teacher teacher = createTeacher();
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));

        Teacher returnedTeacher = teacherService.findById(teacher.getId());

        assertEquals(returnedTeacher.getId(), teacher.getId());
        assertEquals(returnedTeacher.getFirstName(), teacher.getFirstName());
        assertEquals(returnedTeacher.getLastName(), teacher.getLastName());
        assertFalse(returnedTeacher.getLanguages().isEmpty());

        verify(teacherRepository, times(1)).findById(teacher.getId());
    }

    @Test
    void testFindById_shouldThrowExceptionWhenTeacherNotFoundInDatabaseById() {
        String msgException = "Teacher not found!";
        int teacherId = 1;
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> teacherService.findById(teacherId)
        );
        assertEquals(exception.getMessage(), msgException);

        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void testFindActiveById_shouldReturnActiveTeacherFromDatabaseById() {
        Teacher teacher = createTeacher();
        when(teacherRepository.findByIdAndActiveTrue(teacher.getId())).thenReturn(Optional.of(teacher));

        Teacher returnedTeacher = teacherService.findActiveById(teacher.getId());

        assertEquals(returnedTeacher.getId(), teacher.getId());
        assertEquals(returnedTeacher.getFirstName(), teacher.getFirstName());
        assertEquals(returnedTeacher.getLastName(), teacher.getLastName());
        assertFalse(returnedTeacher.getLanguages().isEmpty());

        verify(teacherRepository, times(1)).findByIdAndActiveTrue(teacher.getId());
    }

    @Test
    void testFindActiveById_shouldThrowExceptionWhenTeacherIsNotAvailableOrNotFoundInDatabase() {
        String msgException = "Teacher not found or not available!";
        int teacherId = 1;
        when(teacherRepository.findByIdAndActiveTrue(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> teacherService.findActiveById(teacherId)
        );
        assertEquals(exception.getMessage(), msgException);

        verify(teacherRepository, times(1)).findByIdAndActiveTrue(teacherId);
    }

    @Test
    void testFindWithLockingById_shouldBlockAndReturnTeacherFromDatabaseById() {
        Teacher teacher = createTeacher();
        when(teacherRepository.findWithLockingByIdAndActiveTrue(teacher.getId())).thenReturn(Optional.of(teacher));

        Teacher returnedTeacher = teacherService.findWithLockingById(teacher.getId());

        assertEquals(returnedTeacher.getId(), teacher.getId());
        assertEquals(returnedTeacher.getFirstName(), teacher.getFirstName());
        assertEquals(returnedTeacher.getLastName(), teacher.getLastName());
        assertFalse(returnedTeacher.getLanguages().isEmpty());

        verify(teacherRepository, times(1)).findWithLockingByIdAndActiveTrue(teacher.getId());
    }

    @Test
    void testFindWithLockingById_shouldThrowExceptionWhenTeacherNotFoundInDatabaseById() {
        String msgException = "Teacher not found or not available!";
        int teacherId = 1;
        when(teacherRepository.findWithLockingByIdAndActiveTrue(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> teacherService.findWithLockingById(teacherId)
        );
        assertEquals(exception.getMessage(), msgException);

        verify(teacherRepository, times(1)).findWithLockingByIdAndActiveTrue(teacherId);
    }

    @Test
    void testDelete_shouldChangeFieldActiveOnFalse() {
        Teacher teacher = createTeacher();
        when(teacherRepository.findWithLockingByIdAndActiveTrue(teacher.getId())).thenReturn(Optional.of(teacher));

        teacherService.delete(teacher.getId());

        verify(teacherRepository, times(1)).save(teacher);
    }

    private Teacher createTeacher() {
        return Teacher.builder()
                .id(1)
                .firstName("Dariusz")
                .lastName("Gontarczyk")
                .languages(Set.of(Language.JAVA, Language.KOTLIN))
                .active(true)
                .build();
    }

    private List<Teacher> createListWithTeachers() {
        Teacher firstTeacher = createTeacher();

        return List.of(
                firstTeacher,
                Teacher.builder()
                        .id(firstTeacher.getId() + 1)
                        .firstName("Krzysztof")
                        .lastName("Gontarczyk")
                        .languages(Set.of(Language.JAVA))
                        .active(true)
                        .build()
        );
    }
}