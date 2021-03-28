package nextstep.subway.auth.ui.authentication;

import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.converter.AuthenticationConverter;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor{

    AuthenticationConverter authenticationConverter;
    UserDetailService userDetailsService;

    public AuthenticationInterceptor(AuthenticationConverter authenticationConverter, UserDetailService userDetailsService){
        this.authenticationConverter = authenticationConverter;
        this.userDetailsService = userDetailsService;
    }

    /*
     * preHandle
     *  - convert : request -> AuthenticationToken
     *  - authenticate : AuthenticationToken -> Authentication
     *  - afterAuthentication : 세션) 세션에 인증토큰 담기 / 토큰) 응답에 인증토큰 담기
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authentication = authenticate(token);

        if (authentication == null) {
            throw new RuntimeException();
        }

        afterAuthentication(request, response, authentication);

        return false;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new IllegalArgumentException("인증정보가 올바르지 않습니다.");
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new IllegalArgumentException("패스워드가 올바르지 않습니다.");
        }
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

}
