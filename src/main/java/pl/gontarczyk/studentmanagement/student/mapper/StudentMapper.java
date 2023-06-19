package pl.gontarczyk.studentmanagement.student.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.student.model.StudentDto;
import pl.gontarczyk.studentmanagement.student.model.StudentViewDto;

@Mapper
public interface StudentMapper {

    StudentDto toDto(Student student);

    Student toEntity(StudentDto studentDto);

    @Mapping(target = "teacher", expression = "java(student.getTeacher().getFirstName() + \" \" + student.getTeacher().getLastName())")
    StudentViewDto toViewDto(Student student);
}