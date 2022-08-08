package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationFilter implements HandlerInterceptor {
    private LoginMemberService loginMemberService;

    public BasicAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 해당 Type(Basic) 으로 요청된 header 의 인증 요청값을 불러다 디코딩하고,
            String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
            String authHeader = new String(Base64.decodeBase64(authCredentials));

            // principal(email), credentials(password) 를 분리한 후
            String[] splits = authHeader.split(":");
            String principal = splits[0];
            String credentials = splits[1];

            // 이 값들로 인증토큰을 만들고
            AuthenticationToken token = new AuthenticationToken(principal, credentials);

            // 인증 토큰의 principal(email)로 멤버 정보를 조회해 존재하는지 확인하고 credentials(password)가 일치하는지 확인한다.
            LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());
            if (loginMember == null) {
                throw new AuthenticationException();
            }

            if (!loginMember.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            // 인증이 완료된 해당 요청에 대해 회원 정보가 포함된 인증완료 정보를 SecurityContextHolder 의 Context 안에 넣어준다.
            Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
