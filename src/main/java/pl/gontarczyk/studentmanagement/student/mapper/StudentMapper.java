package pl.gontarczyk.studentmanagement.student.mapper;

import org.mapstruct.Mapper;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.student.model.StudentDto;

@Mapper
public interface StudentMapper {

    StudentDto toDto(Student student);

    Student toEntity(StudentDto studentDto);
}