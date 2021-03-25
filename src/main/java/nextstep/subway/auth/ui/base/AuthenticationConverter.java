package nextstep.subway.auth.ui.base;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationConverter {

    AuthenticationToken convert(HttpServletRequest request);

}
