package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.TokenInvalidException;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);

        final String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        final String token = jwtTokenProvider.createToken(payload);

        validToken(token);

        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        final TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        final String principal = authenticationToken.getPrincipal();
        return new Authentication(customUserDetailsService.loadUserByUsername(principal));
    }

    private void validToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new TokenInvalidException();
        }
    }
}
