package vstu.lab.utility;

import javafx.scene.control.TextInputControl;

/**
 * TVLab_1 created by Dmitry on 24.02.2016.
 */
public class FXValidator {

    // Состояния валидатора
    public static final short INVALID = 0;
    public static final short INTERMEDIATE = 1;
    public static final short ACCEPTABLE = 2;

    private TextInputControl field;
    private Validator validator;

    public FXValidator(TextInputControl field, Validator validator) {
        this.field = field;
        this.validator = validator;
        this.field.textProperty().addListener((observable, oldValue, newValue) -> {
            if(validator.validate(newValue) == INVALID)
                field.setText(oldValue);
        });
    }

    public boolean isValid() {
        return validator.validate(field.getText()) == ACCEPTABLE;
    }

    public short getState() {
        return validator.validate(field.getText());
    }

    public interface Validator {
        short validate(String input);
    }
}
