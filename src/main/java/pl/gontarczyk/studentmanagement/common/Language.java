package pl.gontarczyk.studentmanagement.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.transaction.Status;

import java.util.Arrays;
import java.util.Optional;

public enum Language {

    JAVA,
    KOTLIN,
    PYTHON,
    PHP;

    public static boolean isExists(String teacherLanguage) {
        return Arrays.stream(Language.values())
                .anyMatch(language -> language.name().equals(teacherLanguage));
    }
}