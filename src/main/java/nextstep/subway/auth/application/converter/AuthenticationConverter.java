package nextstep.subway.auth.application.converter;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationConverter {

    /**
     * parse token from http request.
     * @param request http request from client
     * @return token
     */
    AuthenticationToken convert(HttpServletRequest request);
}
