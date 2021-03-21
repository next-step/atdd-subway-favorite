package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.base.AuthenticationInterceptor;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptorV2 extends AuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptorV2(UserDetailService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(customUserDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);

        TokenResponse tokenResponse = TokenResponse.of(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(),TokenRequest.class);

        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }
}
