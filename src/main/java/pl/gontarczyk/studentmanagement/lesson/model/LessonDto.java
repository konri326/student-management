package pl.gontarczyk.studentmanagement.lesson.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LessonDto {

    @NotNull(message = "You must give date!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Future(message = "You can't save lesson in the past!")
    private LocalDateTime dateTime;
}