package nextstep.auth.authentication.converter;

import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationConverter {

    AuthenticationToken convert(HttpServletRequest request);

}
