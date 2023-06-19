package pl.gontarczyk.studentmanagement.teacher.model;

import jakarta.persistence.*;
import lombok.*;
import pl.gontarczyk.studentmanagement.common.Language;
import pl.gontarczyk.studentmanagement.student.model.Student;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "teacher_language", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "language")
    private Set<Language> languages;

    @OneToMany(mappedBy = "teacher")
    private Set<Student> students;

    private boolean active;

    public String fullName() {
        return getFirstName() + " " + getLastName();
    }
}