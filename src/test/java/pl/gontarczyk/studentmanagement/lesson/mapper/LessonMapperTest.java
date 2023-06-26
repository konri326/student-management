package pl.gontarczyk.studentmanagement.lesson.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.lesson.model.Lesson;
import pl.gontarczyk.studentmanagement.lesson.model.LessonDto;
import pl.gontarczyk.studentmanagement.lesson.model.LessonViewDto;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(classes = { LessonMapperImpl.class })
class LessonMapperTest {

    @Autowired
    private LessonMapper lessonMapper;

    @Test
    void testToEntity_shouldMapDtoToEntity() {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        LessonDto lessonDto = LessonDto.builder()
                .dateTime(dateTimeNow)
                .build();

        Lesson lesson = lessonMapper.toEntity(lessonDto);

        assertEquals(lesson.getDateTime(), lessonDto.getDateTime());
    }

    @Test
    void testToDto_shouldMapEntityToDto() {
        Lesson lesson = createLesson();

        LessonDto lessonDto = lessonMapper.toDto(lesson);

        assertEquals(lessonDto.getDateTime(), lesson.getDateTime());
    }

    @Test
    void testToViewDto_shouldMapEntityToViewDto() {
        Lesson lesson = createLesson();
        String dateTimeOutPut = lesson.getDateTime().toLocalDate().toString() + " " + lesson.getDateTime().toLocalTime().toString();
        String studentFullName = lesson.getStudent().getFirstName() + " " + lesson.getStudent().getLastName();
        String teacherFullName = lesson.getTeacher().getFirstName() + " " + lesson.getTeacher().getLastName();

        LessonViewDto lessonViewDto = lessonMapper.toViewDto(lesson);

        assertEquals(lessonViewDto.getDateTime(), dateTimeOutPut);
        assertEquals(lessonViewDto.getStudent(), studentFullName);
        assertEquals(lessonViewDto.getTeacher(), teacherFullName);
    }

    private Lesson createLesson() {
        return Lesson.builder()
                .id(1)
                .dateTime(LocalDateTime.now())
                .student(Student.builder()
                        .id(1)
                        .firstName("Konrad")
                        .lastName("Gontarczyk")
                        .language(Language.JAVA)
                        .build())
                .teacher(Teacher.builder()
                        .firstName("Dariusz")
                        .lastName("Gontarczyk")
                        .languages(Set.of(Language.JAVA))
                        .build())
                .build();
    }
}