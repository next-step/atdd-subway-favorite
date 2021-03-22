package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationInterceptor;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(userDetailsService, new TokenAuthenticationConverter());
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication == null) {
            throw new RuntimeException();
        }

        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
