package pl.gontarczyk.studentmanagement.student.model;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.gontarczyk.studentmanagement.validation.SupportedLanguage;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentDto {

    @Pattern(regexp = "[A-Za-z]{3,}", message = "Invalid first name!")
    private String firstName;

    @Pattern(regexp = "[A-Za-z]{3,}", message = "Invalid first name!")
    private String lastName;

    @SupportedLanguage
    private String language;
}