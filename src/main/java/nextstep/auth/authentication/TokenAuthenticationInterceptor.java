package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);

        // TODO: authentication으로 TokenResponse 추출하기
        TokenResponse tokenResponse = null;

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        // TODO: request에서 AuthenticationToken 객체 생성하기
        TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);

        return AuthenticationToken.of(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        // TODO: AuthenticationToken에서 AuthenticationToken 객체 생성하기
        String principal = authenticationToken.getPrincipal();
        LoginMember member = customUserDetailsService.loadUserByUsername(principal);

        validateAuthentication(member, authenticationToken);

        return new Authentication(member);
    }

    private void validateAuthentication(LoginMember member, AuthenticationToken token) {
        if(!member.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
