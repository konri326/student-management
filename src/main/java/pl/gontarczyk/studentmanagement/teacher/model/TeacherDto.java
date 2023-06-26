package pl.gontarczyk.studentmanagement.teacher.model;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.gontarczyk.studentmanagement.validation.SupportedLanguages;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TeacherDto {

    @Pattern(regexp = "[A-Za-z]{3,}", message = "Invalid first name!")
    private String firstName;

    @Pattern(regexp = "[A-Za-z]{3,}", message = "Invalid last name!")
    private String lastName;

    @SupportedLanguages
    private Set<String> languages;

    private boolean active;
}