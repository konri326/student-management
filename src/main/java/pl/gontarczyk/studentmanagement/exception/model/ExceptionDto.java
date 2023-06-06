package pl.gontarczyk.studentmanagement.exception.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionDto {

    private final LocalDateTime dateTime = LocalDateTime.now();
    private final String message;
}