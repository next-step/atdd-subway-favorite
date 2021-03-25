package nextstep.subway.auth.ui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import nextstep.subway.auth.domain.AuthenticationToken;

public interface AuthenticationConverter {
	AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
