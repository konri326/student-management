package pl.gontarczyk.studentmanagement.student.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.student.model.StudentDto;
import pl.gontarczyk.studentmanagement.student.model.StudentViewDto;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(classes = { StudentMapperImpl.class })
class StudentMapperTest {

    @Autowired
    private StudentMapper studentMapper;

    @Test
    void testToDto_shouldMapEntityToDto() {
        Student student = createStudent();

        StudentDto studentDto = studentMapper.toDto(student);

        assertEquals(studentDto.getFirstName(), student.getFirstName());
        assertEquals(studentDto.getLastName(), student.getLastName());
        assertEquals(studentDto.getLanguage(), student.getLanguage().name());
    }

    @Test
    void testToEntity_shouldMapDtoToEntity() {
        StudentDto studentDto = StudentDto.builder()
                .firstName("Konrad")
                .lastName("Gontarczyk")
                .language(Language.JAVA.name())
                .build();

        Student student = studentMapper.toEntity(studentDto);

        assertEquals(student.getFirstName(), studentDto.getFirstName());
        assertEquals(student.getLastName(), studentDto.getLastName());
        assertEquals(student.getLanguage().name(), studentDto.getLanguage());
    }

    @Test
    void testToViewDto_shouldMapEntityToViewDto() {
        Student student = createStudent();
        String teacherFullName = student.getTeacher().getFirstName() + " " + student.getTeacher().getLastName();

        StudentViewDto studentViewDto = studentMapper.toViewDto(student);

        assertEquals(studentViewDto.getFirstName(), student.getFirstName());
        assertEquals(studentViewDto.getLastName(), student.getLastName());
        assertEquals(studentViewDto.getLanguage(), student.getLanguage().name());
        assertEquals(studentViewDto.getTeacher(), teacherFullName);
    }

    private Student createStudent() {
        return Student.builder()
                .id(1)
                .firstName("Konrad")
                .lastName("Gontarczyk")
                .language(Language.JAVA)
                .teacher(Teacher.builder()
                        .firstName("Dariusz")
                        .lastName("Gontarczyk")
                        .build())
                .build();
    }
}