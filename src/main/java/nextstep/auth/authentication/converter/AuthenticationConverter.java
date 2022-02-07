package nextstep.auth.authentication.converter;

import javax.servlet.http.HttpServletRequest;

import nextstep.auth.authentication.AuthenticationToken;

public interface AuthenticationConverter {
    boolean matchRequestUri(String url);

    AuthenticationToken convert(HttpServletRequest request);
}
