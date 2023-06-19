package pl.gontarczyk.studentmanagement.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.gontarczyk.studentmanagement.exception.model.ExceptionDto;
import pl.gontarczyk.studentmanagement.exception.model.ValidationExceptionDto;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handlerArgumentNotValidException(MethodArgumentNotValidException ex) {
        ValidationExceptionDto exceptionDto = new ValidationExceptionDto();
        ex.getFieldErrors()
                .forEach(error -> exceptionDto.addErrorInfo(error.getField(), error.getDefaultMessage()));
        return exceptionDto;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handlerEntityNotFoundException(EntityNotFoundException ex) {
        return new ExceptionDto(ex.getMessage());
    }

    @ExceptionHandler(IncorrectConnectionOfObjectsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handlerIncorrectConnectionOfObjectsException(IncorrectConnectionOfObjectsException ex) {
        return new ExceptionDto(ex.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handlerEntityExistsException(EntityExistsException ex) {
        return new ExceptionDto(ex.getMessage());
    }
}