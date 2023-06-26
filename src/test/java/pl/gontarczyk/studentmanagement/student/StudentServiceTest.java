package pl.gontarczyk.studentmanagement.student;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.teacher.TeacherService;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherService teacherService;

    @InjectMocks StudentService studentService;

    @Test
    void testFindAll_shouldReturnListWithAllStudents() {
        List<Student> students = createListWithStudents();
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> returnedStudents = studentService.findAll();

        assertEquals(returnedStudents.size(), students.size());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testSave_shouldSaveCorrectStudentInDatabase() {
        Student student = createStudent();
        Teacher teacher = createTeacher();
        when(studentRepository.save(student)).thenReturn(student);
        when(teacherService.findActiveById(teacher.getId())).thenReturn(teacher);

        Student returnedStudent = studentService.save(student, teacher.getId());

        assertEquals(returnedStudent.getId(), student.getId());
        assertEquals(returnedStudent.getFirstName(), student.getFirstName());
        assertEquals(returnedStudent.getLastName(), student.getLastName());
        assertEquals(returnedStudent.getTeacher().getFirstName(), teacher.getFirstName());

        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testFindById_shouldReturnStudentFromDatabaseById() {
        Student student = createStudent();
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        Student returnedStudent = studentService.findById(student.getId());

        assertEquals(returnedStudent.getId(), student.getId());
        assertEquals(returnedStudent.getFirstName(), student.getFirstName());
        assertEquals(returnedStudent.getLastName(), student.getLastName());
        assertEquals(returnedStudent.getLanguage(), student.getLanguage());

        verify(studentRepository, times(1)).findById(student.getId());
    }

    @Test
    void testFindById_shouldThrowExceptionWhenStudentNotFoundInDatabaseById() {
        String exceptionMsg = "Student not found!";
        int studentId = 1;
        when(studentRepository.findById(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.findById(studentId)
        );
        assertEquals(exception.getMessage(), exceptionMsg);

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testFindActiveById_shouldReturnActiveStudentFromDatabaseById() {
        Student student = createStudent();
        when(studentRepository.findByIdAndActiveTrue(student.getId())).thenReturn(Optional.of(student));

        Student returnedStudent = studentService.findActiveById(student.getId());

        assertEquals(returnedStudent.getId(), student.getId());
        assertEquals(returnedStudent.getFirstName(), student.getFirstName());
        assertEquals(returnedStudent.getLastName(), student.getLastName());
        assertEquals(returnedStudent.getLanguage(), student.getLanguage());

        verify(studentRepository, times(1)).findByIdAndActiveTrue(student.getId());
    }

    @Test
    void testFindActiveById_shouldThrowExceptionWhenStudentIsNotAvailableOrNotFoundInDatabase() {
        String msgException = "Student not found or not available!";
        int studentId = 1;
        when(studentRepository.findByIdAndActiveTrue(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.findActiveById(studentId)
        );
        assertEquals(exception.getMessage(), msgException);

        verify(studentRepository, times(1)).findByIdAndActiveTrue(studentId);
    }

    @Test
    void testFindWithLockingById_shouldBlockAndReturnStudentFromDatabaseById() {
        Student student = createStudent();
        when(studentRepository.findWithLockingByIdAndActiveTrue(student.getId())).thenReturn(Optional.of(student));

        Student returnedStudent = studentService.findWithLockingById(student.getId());

        assertEquals(returnedStudent.getId(), student.getId());
        assertEquals(returnedStudent.getFirstName(), student.getFirstName());
        assertEquals(returnedStudent.getLastName(), student.getLastName());
        assertEquals(returnedStudent.getLanguage(), student.getLanguage());

        verify(studentRepository, times(1)).findWithLockingByIdAndActiveTrue(student.getId());
    }

    @Test
    void testFindWithLockingById_shouldThrowExceptionWhenStudentNotFoundInDatabaseById() {
        String msgException = "Student not found or not available!";
        int studentId = 1;
        when(studentRepository.findWithLockingByIdAndActiveTrue(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.findWithLockingById(studentId)
        );
        assertEquals(exception.getMessage(), msgException);

        verify(studentRepository, times(1)).findWithLockingByIdAndActiveTrue(studentId);
    }

    @Test
    void testDelete_shouldChangeFieldActiveOnFalse() {
        Student student = createStudent();
        when(studentRepository.findWithLockingByIdAndActiveTrue(student.getId())).thenReturn(Optional.of(student));

        studentService.delete(student.getId());

        verify(studentRepository, times(1)).save(student);
    }

    private Student createStudent() {
        return Student.builder()
                .id(1)
                .firstName("Konrad")
                .lastName("Gontarczyk")
                .language(Language.JAVA)
                .active(true)
                .build();
    }

    private List<Student> createListWithStudents() {
        Student firstStudent = createStudent();

        return List.of(
                firstStudent,
                Student.builder()
                        .id(firstStudent.getId() + 1)
                        .firstName("Krzysztof")
                        .lastName("Gontarczyk")
                        .language(Language.KOTLIN)
                        .active(true)
                        .build()
        );
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
}