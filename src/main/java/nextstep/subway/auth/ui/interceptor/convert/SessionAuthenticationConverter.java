package nextstep.subway.auth.ui.interceptor.convert;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class SessionAuthenticationConverter implements AuthenticationConverter{

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        return null;
    }
}
