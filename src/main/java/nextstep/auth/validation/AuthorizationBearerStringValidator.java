package nextstep.auth.validation;

import nextstep.auth.exception.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationBearerStringValidator extends AuthorizationValidator {

    @Override
    protected void validate(String authorization) {
        String authPrefix = authorization.split(" ")[0];

        if (!"bearer".equalsIgnoreCase(authPrefix)) {
            throw new AuthenticationException();
        }
    }
}
