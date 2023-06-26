package pl.gontarczyk.studentmanagement.teacher.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;
import pl.gontarczyk.studentmanagement.teacher.model.TeacherDto;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = { TeacherMapperImpl.class })
class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void testToEntity_shouldMapDtoToEntity() {
        TeacherDto teacherDto = TeacherDto.builder()
                .firstName("Dariusz")
                .lastName("Gontarczyk")
                .languages(Set.of(Language.JAVA.name()))
                .active(true)
                .build();

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getLanguages().size(), teacherDto.getLanguages().size());
        assertTrue(teacher.getLanguages().contains(Language.JAVA));
        assertEquals(teacher.isActive(), teacherDto.isActive());
    }

    @Test
    void testToDto_shouldMapEntityToDto() {
        Teacher teacher = Teacher.builder()
                .firstName("Dariusz")
                .lastName("Gontarczyk")
                .languages(Set.of(Language.JAVA))
                .active(true)
                .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getLanguages().size(), teacher.getLanguages().size());
        assertFalse(teacherDto.getLanguages().isEmpty());
        assertEquals(teacherDto.isActive(), teacher.isActive());
    }
}