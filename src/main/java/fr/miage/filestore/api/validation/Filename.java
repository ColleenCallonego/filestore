package fr.miage.filestore.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy={})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp=ValidationPattern.FILE_PATTERN, message = "{invalid.filename}")
public @interface Filename {
    String message() default "{invalid.filename}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
