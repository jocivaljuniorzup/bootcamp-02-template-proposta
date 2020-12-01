package br.com.zup.jocivaldias.proposal.shared.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CpfCnpjValidator.class)
public @interface CpfCnpj {

    String message() default "Invalid CPF/CNPJ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
