package pl.gontarczyk.studentmanagement.teacher.mapper;

import org.mapstruct.Mapper;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;
import pl.gontarczyk.studentmanagement.teacher.model.TeacherDto;

@Mapper
public interface TeacherMapper {

    Teacher toEntity(TeacherDto teacherDto);
    TeacherDto toDto(Teacher teacher);
}