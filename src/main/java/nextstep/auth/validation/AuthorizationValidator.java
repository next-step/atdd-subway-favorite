package nextstep.auth.validation;

public abstract class AuthorizationValidator {

    private AuthorizationValidator nextValidator = null;

    public AuthorizationValidator setNext(AuthorizationValidator validator) {
        this.nextValidator = validator;
        return validator;
    }

    protected abstract void validate(String authorization);

    public void execute(String authorization) {
        validate(authorization);

        if (nextValidator != null)
            nextValidator.execute(authorization);
    }
}
