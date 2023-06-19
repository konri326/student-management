package pl.gontarczyk.studentmanagement.lesson.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LessonDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Future(message = "You can't save lesson in the past!")
    private LocalDateTime dateTime;
}