package pl.gontarczyk.studentmanagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.gontarczyk.studentmanagement.common.Language;

import java.util.Set;

public class SupportedLanguageValidator implements ConstraintValidator<SupportedLanguage, Set<String>> {

    @Override
    public boolean isValid(Set<String> languages, ConstraintValidatorContext constraintValidatorContext) {
        if (languages != null && languages.size() >= 1) {
            return languages.stream()
                    .allMatch(Language::isExists);
        } else {
            return false;
        }
    }
}