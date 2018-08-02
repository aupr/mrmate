package com.wiexon.app.JFValidator;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty("icon")
public class ComPortValidator extends ValidatorBase {
    public ComPortValidator() {
    }

    protected void eval() {
        if (this.srcControl.get() instanceof TextInputControl) {
            this.evalTextInputField();
        }

        if (this.srcControl.get() instanceof JFXComboBox) {
            this.evalComboBoxField();
        }

    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if (ValidationLogic.isValidComPortAddress(textField.getText())) {
            this.hasErrors.set(false);
        } else {
            this.hasErrors.set(true);
        }
    }

    private void evalComboBoxField() {
        JFXComboBox<?> comboField = (JFXComboBox)this.srcControl.get();
        boolean hasValue = comboField.getValue() != null;
        boolean editorHasNonEmptyText = comboField.isEditable() && comboField.getEditor().getText() != null && !comboField.getEditor().getText().isEmpty();
        this.hasErrors.set(!hasValue && !editorHasNonEmptyText);
    }
}
