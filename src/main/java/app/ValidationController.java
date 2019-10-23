package app;

import model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import validation.ValidationErrors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;

@RestController
public class ValidationController {

    @PostMapping("manual-validation")
    public ResponseEntity<Object> manualValidation(@RequestBody Order order) {

        ValidationErrors errors = new ValidationErrors();
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(order);

        for (ConstraintViolation<Order> violation : violations) {
            errors.addErrorMessage(violation.getMessage());
        }

        if (errors.hasErrors()) {
            return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PostMapping("validation")
    public void validation(@RequestBody @Valid Order order) {

    }
}
