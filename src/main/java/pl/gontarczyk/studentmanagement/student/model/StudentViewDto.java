package pl.gontarczyk.studentmanagement.student.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentViewDto {

    private String firstName;
    private String lastName;
    private String language;
    private String teacher;

}