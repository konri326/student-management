package pl.gontarczyk.studentmanagement.lesson.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LessonViewDto {

    private String dateTime;
    private String student;
    private String teacher;
}