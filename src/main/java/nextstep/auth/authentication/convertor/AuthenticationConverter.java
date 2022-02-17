package nextstep.auth.authentication.convertor;

import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request);
}
