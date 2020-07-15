package nextstep.subway.auth.application.converter;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationTokenConverter {
    AuthenticationToken convert(HttpServletRequest request);
}
