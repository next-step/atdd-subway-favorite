package nextstep.subway.auth.ui.interceptor.convert;

import javax.servlet.http.HttpServletRequest;

import nextstep.subway.auth.domain.AuthenticationToken;

public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request);
}
