package nextstep.auth.validation;

import nextstep.auth.exception.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationNullValidator extends AuthorizationValidator {

    @Override
    protected void validate(String authorization) {
        if (authorization == null) {
            throw new AuthenticationException();
        }
    }
}
