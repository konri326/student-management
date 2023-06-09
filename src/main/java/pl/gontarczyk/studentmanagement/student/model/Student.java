package pl.gontarczyk.studentmanagement.student.model;

import jakarta.persistence.*;
import lombok.*;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.lesson.model.Lesson;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.util.Set;

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

    @ManyToOne
    private Teacher teacher;

    @OneToMany(mappedBy = "student")
    private Set<Lesson> lessons;

    private boolean active;
}