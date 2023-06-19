package pl.gontarczyk.studentmanagement.lesson.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import pl.gontarczyk.studentmanagement.student.model.Student;
import pl.gontarczyk.studentmanagement.teacher.model.Teacher;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Teacher teacher;
}