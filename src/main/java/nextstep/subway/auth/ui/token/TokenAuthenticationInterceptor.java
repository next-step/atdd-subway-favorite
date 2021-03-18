package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
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
        String principal = "";
        String credentials = "";

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        // TODO: AuthenticationToken에서 AuthenticationToken 객체 생성하기
        return new Authentication(null);
    }
}
