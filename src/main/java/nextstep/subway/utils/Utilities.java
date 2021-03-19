package nextstep.subway.utils;

import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class Utilities {

    public static LoginMember getLoginMember(HttpServletRequest request) {
        SecurityContext ctx = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        LoginMember loginMember = (LoginMember) ctx.getAuthentication().getPrincipal();
        return loginMember;
    }
}
