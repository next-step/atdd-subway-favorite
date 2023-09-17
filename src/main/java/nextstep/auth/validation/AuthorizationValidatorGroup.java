package nextstep.auth.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationValidatorGroup {

    private final AuthorizationNullValidator authorizationNullValidator;
    private final AuthorizationBearerStringValidator authorizationBearerStringValidator;
    private final AuthorizationTokenValidator authorizationTokenValidator;

    private void chaining() {
        authorizationNullValidator
            .setNext(authorizationBearerStringValidator)
            .setNext(authorizationTokenValidator);
    }

    public void execute(String authorization) {
        chaining();
        authorizationNullValidator.execute(authorization);
    }
}
