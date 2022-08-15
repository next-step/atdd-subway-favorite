package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UsernamePasswordAuthenticationFilter extends Authenticator {

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        super(loginMemberService);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        return new AuthenticationToken(request.getParameter("email"), request.getParameter("password"));
    }

    @Override
    public void authenticate(LoginMember member, HttpServletResponse response) throws IOException {
        Authentication authentication = new Authentication(member.getEmail(), member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
