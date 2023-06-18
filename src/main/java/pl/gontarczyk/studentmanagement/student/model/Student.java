package pl.gontarczyk.studentmanagement.student.model;

import jakarta.persistence.*;
import lombok.*;
import pl.gontarczyk.studentmanagement.common.Language;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Language language;

    private boolean active;
}