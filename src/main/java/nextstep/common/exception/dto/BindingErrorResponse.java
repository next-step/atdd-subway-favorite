package nextstep.common.exception.dto;

import java.util.List;

public class BindingErrorResponse {
    private final String code;
    private final List<ValidationError> errors;

    public BindingErrorResponse(String code, List<ValidationError> errors) {
        this.code = code;
        this.errors = errors;
    }

    public String getCode() {
        return code;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public static class ValidationError {
        private final String fieldName;
        private final String message;

        public ValidationError(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getMessage() {
            return message;
        }
    }
}
