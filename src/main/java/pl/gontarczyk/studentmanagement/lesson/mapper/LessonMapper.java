package pl.gontarczyk.studentmanagement.lesson.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.gontarczyk.studentmanagement.lesson.model.Lesson;
import pl.gontarczyk.studentmanagement.lesson.model.LessonDto;
import pl.gontarczyk.studentmanagement.lesson.model.LessonViewDto;

@Mapper
public interface LessonMapper {

    Lesson toEntity(LessonDto lessonDto);

    LessonDto toDto(Lesson lesson);

    @Mapping(target = "dateTime", expression = "java(lesson.getDateTime().toLocalDate().toString() + \" \" + lesson.getDateTime().toLocalTime().toString())")
    @Mapping(target = "student", expression = "java(lesson.getStudent().getFirstName() + \" \" + lesson.getStudent().getLastName())")
    @Mapping(target = "teacher", expression = "java(lesson.getTeacher().getFirstName() + \" \" + lesson.getTeacher().getLastName())")
    LessonViewDto toViewDto(Lesson lesson);
}