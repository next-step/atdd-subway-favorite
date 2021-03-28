package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.exception.AuthenticationException;
import nextstep.subway.member.application.UserDetailsService;
import nextstep.subway.auth.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationConverter authenticationConverter;
    private final UserDetailsService userDetailsService;

    protected AuthenticationInterceptor(AuthenticationConverter authenticationConverter, UserDetailsService userDetailsService) {
        this.authenticationConverter = authenticationConverter;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        afterAuthentication(request, response, authentication);

        return false;
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        User loginMember = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
        checkAuthentication(loginMember, authenticationToken);
        return new Authentication(loginMember);
    }

    private void checkAuthentication(User userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException("사용자가 등록되어 있지 않습니다");
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException("비밀번호가 틀립니다");
        }
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
