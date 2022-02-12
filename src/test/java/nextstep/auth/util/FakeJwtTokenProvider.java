package nextstep.auth.util;

import nextstep.auth.token.JwtTokenProvider;

import static nextstep.auth.util.AuthFixture.JWT_TOKEN;

public class FakeJwtTokenProvider extends JwtTokenProvider {

    @Override
    public String createToken(String payload) {
        return JWT_TOKEN;
    }
}
