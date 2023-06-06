package pl.gontarczyk.studentmanagement.exception.model;

import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationExceptionDto extends ExceptionDto {

    private final List<ErrorInfo> errors = new ArrayList<>();

    public ValidationExceptionDto() {
        super("Invalid validation");
    }

    public void addErrorInfo(String field, String message) {
        errors.add(new ErrorInfo(field, message));
    }

    @Value
    public static class ErrorInfo {
        String field;
        String message;
    }
}