package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class FormAuthenticationConverter implements AuthenticationConverter {
    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        return null;
    }
}
