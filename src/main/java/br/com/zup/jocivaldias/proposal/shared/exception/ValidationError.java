package br.com.zup.jocivaldias.proposal.shared.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Long timeStamp, Integer status, String error, String message, String path) {
        super(timeStamp, status, error, message, path);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message) {
        this.errors.add(new FieldMessage(fieldName, message));
    }
}
