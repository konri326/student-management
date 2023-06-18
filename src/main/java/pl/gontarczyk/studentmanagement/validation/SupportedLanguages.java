package pl.gontarczyk.studentmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SupportedLanguagesValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedLanguages {

    String message() default "One or more of the listed languages are not supported or none has been placed!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}