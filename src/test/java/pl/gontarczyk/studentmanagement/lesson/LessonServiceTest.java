package pl.gontarczyk.studentmanagement.lesson;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.lesson.model.Lesson;
import pl.gontarczyk.studentmanagement.student.StudentService;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.teacher.TeacherService;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void testFindAll_shouldReturnListWithAllLessons() {
        List<Lesson> lessons = createListWithLessons();
        when(lessonRepository.findAll()).thenReturn(lessons);

        List<Lesson> returnedLessons = lessonService.findAll();

        assertEquals(returnedLessons.size(), lessons.size());

        verify(lessonRepository, times(1)).findAll();
    }

    @Test
    void testSave_shouldSaveCorrectLessonInDatabase() {
        Lesson lesson = createLesson();
        when(studentService.findWithLockingById(lesson.getStudent().getId())).thenReturn(lesson.getStudent());
        when(teacherService.findWithLockingById(lesson.getTeacher().getId())).thenReturn(lesson.getTeacher());
        when(lessonRepository.save(lesson)).thenReturn(lesson);

        Lesson returnedLesson = lessonService.save(lesson, lesson.getStudent().getId());

        assertEquals(returnedLesson.getId(), lesson.getId());
        assertEquals(returnedLesson.getDateTime(), lesson.getDateTime());
        assertEquals(returnedLesson.getStudent(), lesson.getStudent());
        assertEquals(returnedLesson.getTeacher(), lesson.getTeacher());

        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void testSave_shouldThrowExceptionWhenLessonOverlapWithAnother() {
        String exceptionMsg = "Lesson is overlap with another!";
        Lesson lesson = createLesson();
        LocalDateTime checkFrom = lesson.getDateTime().minusMinutes(59);
        LocalDateTime checkTo = lesson.getDateTime().plusMinutes(59);
        when(studentService.findWithLockingById(lesson.getStudent().getId())).thenReturn(lesson.getStudent());
        when(teacherService.findWithLockingById(lesson.getTeacher().getId())).thenReturn(lesson.getTeacher());
        when(lessonRepository.existsByTeacherAndDateTimeBetween(lesson.getTeacher(), checkFrom, checkTo)).thenReturn(true);

        EntityExistsException exception = assertThrows(
                EntityExistsException.class,
                () -> lessonService.save(lesson, lesson.getStudent().getId())
        );
        assertEquals(exception.getMessage(), exceptionMsg);

        verify(lessonRepository, times(1)).existsByTeacherAndDateTimeBetween(lesson.getTeacher(), checkFrom, checkTo);
    }

    @Test
    void testUpdate_shouldChangeDateForLessonAndUpdateRecordInDatabase() {
        Lesson lesson = createLesson();
        when(lessonRepository.findWithLockingById(lesson.getId())).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(lesson)).thenReturn(lesson);

        Lesson returnedLesson = lessonService.update(lesson, lesson.getId());

        assertEquals(returnedLesson.getId(), lesson.getId());
        assertEquals(returnedLesson.getDateTime(), lesson.getDateTime());
        assertEquals(returnedLesson.getStudent(), lesson.getStudent());
        assertEquals(returnedLesson.getTeacher(), lesson.getTeacher());

        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void testFindById_shouldReturnLessonFromDatabaseById() {
        Lesson lesson = createLesson();
        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        Lesson returnedLesson = lessonService.findById(lesson.getId());

        assertEquals(returnedLesson.getId(), lesson.getId());
        assertEquals(returnedLesson.getDateTime(), lesson.getDateTime());
        assertEquals(returnedLesson.getStudent(), lesson.getStudent());
        assertEquals(returnedLesson.getTeacher(), lesson.getTeacher());

        verify(lessonRepository, times(1)).findById(lesson.getId());
    }

    @Test
    void testFindById_shouldThrowExceptionWhenLessonNotFoundInDatabase() {
        String msgException = "Lesson not found!";
        int lessonId = 1;
        when(lessonRepository.findById(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> lessonService.findById(lessonId)
        );
        assertEquals(exception.getMessage(), msgException);

        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    void testFindWithLockingById_shouldBlockAndReturnLessonFromDatabaseById() {
        Lesson lesson = createLesson();
        when(lessonRepository.findWithLockingById(lesson.getId())).thenReturn(Optional.of(lesson));

        Lesson returnedLesson = lessonService.findWithLockingById(lesson.getId());

        assertEquals(returnedLesson.getId(), lesson.getId());
        assertEquals(returnedLesson.getDateTime(), lesson.getDateTime());
        assertEquals(returnedLesson.getStudent(), lesson.getStudent());
        assertEquals(returnedLesson.getTeacher(), lesson.getTeacher());

        verify(lessonRepository, times(1)).findWithLockingById(lesson.getId());
    }

    @Test
    void testFindWithLockingById_shouldThrowExceptionWhenLessonNotFoundInDatabase() {
        String msgException = "Lesson not found!";
        int lessonId = 1;
        when(lessonRepository.findWithLockingById(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> lessonService.findWithLockingById(lessonId)
        );
        assertEquals(exception.getMessage(), msgException);

        verify(lessonRepository, times(1)).findWithLockingById(lessonId);
    }

    @Test
    void testDelete_shouldDeleteLessonFromDatabase() {
        Lesson lesson = createLesson();

        lessonService.delete(lesson.getId());

        verify(lessonRepository, times(1)).deleteById(lesson.getId());
    }

    private Lesson createLesson() {
        Student student = createFirstStudent();
        Teacher teacher = createTeacher();

        return Lesson.builder()
                .id(1)
                .dateTime(LocalDateTime.now())
                .student(student)
                .teacher(teacher)
                .build();
    }

    private List<Lesson> createListWithLessons() {
        Lesson lesson = createLesson();
        Student student = createSecondStudent();
        Teacher teacher = createTeacher();

        return List.of(
                lesson,
                Lesson.builder()
                        .id(lesson.getId() + 1)
                        .dateTime(LocalDateTime.now().plusHours(5))
                        .student(student)
                        .teacher(teacher)
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

    private Student createFirstStudent() {
        Teacher teacher = createTeacher();

        return Student.builder()
                .id(1)
                .firstName("Konrad")
                .lastName("Gontarczyk")
                .language(Language.JAVA)
                .teacher(teacher)
                .active(true)
                .build();
    }

    private Student createSecondStudent() {
        Teacher teacher = createTeacher();

        return Student.builder()
                .id(2)
                .firstName("Krzysztof")
                .lastName("Gontarczyk")
                .language(Language.JAVA)
                .teacher(teacher)
                .active(true)
                .build();
    }
}