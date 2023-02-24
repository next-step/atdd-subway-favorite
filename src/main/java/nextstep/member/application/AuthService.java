package nextstep.member.application;

import nextstep.member.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public TokenResponse authByGithub(String code) {
        return new TokenResponse();
    }
}
