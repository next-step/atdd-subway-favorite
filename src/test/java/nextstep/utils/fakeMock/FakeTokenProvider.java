package nextstep.utils.fakeMock;

import nextstep.auth.application.TokenProvider;

public class FakeTokenProvider implements TokenProvider {
    @Override
    public String createToken(String principal) {
        return "createToken_success";
    }

    @Override
    public String getPrincipal(String token) {
        return "getPrincipal_success";
    }

    @Override
    public boolean validateToken(String token) {
        return true;
    }
}

