package nextstep.auth.authentication;

import nextstep.auth.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * Filter 란?
 * 스프링 컨텍스트 안에 있는 디스패처 서블릿 전/후에 url 패턴에 맞는 모든 요청에 부가작업을 처리할 수 있는 기능을 제공
 * 스프링 컨테이너가 관리하는 것이 아닌 웹 컨테이너(톰캣)에 의해 관리된다.
 * 클래스 Name이 필터이고 실질적으로 Interceptor를 의미하는 듯 하다....
 * 사용자 ID와 PASSWORD를 인증하는 필터(인터셉터)
 * Interceptor 란?
 * 디스패처 서블릿이 컨트롤러를 호출하기 전/후 요청과 응답을 참조하거나 가공할 수 있는 기능을 의미한다.
 */
public class UsernamePasswordAuthenticationFilter extends AbstractCreateAuthenticationFilter {

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        if(!isExistAuthentication(email, password)) {
            throw new AuthenticationException();
        }
        return new AuthenticationToken(String.valueOf(email), String.valueOf(password));
    }

    @Override
    protected String returnAuthenticationToken(String principal, List<String> authorities) {
        SecurityContextHolder.getContext().setAuthentication(new Authentication(principal, authorities));
        return "";
    }

    private boolean isExistAuthentication(Object email, Object password) {
        return Objects.nonNull(email) && Objects.nonNull(password);
    }

}
