package pl.gontarczyk.studentmanagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.gontarczyk.studentmanagement.common.Language;

import java.util.Arrays;

public class SupportedLanguageValidator implements ConstraintValidator<SupportedLanguage, String> {

    @Override
    public boolean isValid(String inputLanguage, ConstraintValidatorContext constraintValidatorContext) {
        return Language.isExists(inputLanguage);
    }
}