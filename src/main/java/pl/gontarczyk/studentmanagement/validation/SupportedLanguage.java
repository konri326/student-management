package pl.gontarczyk.studentmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SupportedLanguageValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedLanguage {

    String message() default "Language is not supported!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}