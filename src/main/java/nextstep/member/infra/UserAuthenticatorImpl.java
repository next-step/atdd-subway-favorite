package nextstep.member.infra;

import nextstep.member.application.UserAuthenticator;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticatorImpl implements UserAuthenticator {

    @Override
    public String authenticate(String accessToken) {
        return null;
    }
}
