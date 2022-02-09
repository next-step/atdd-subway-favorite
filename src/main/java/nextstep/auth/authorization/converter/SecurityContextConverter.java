package nextstep.auth.authorization.converter;

import javax.servlet.http.HttpServletRequest;

import nextstep.auth.context.SecurityContext;

public interface SecurityContextConverter {
    SecurityContext convert(HttpServletRequest request);
}
