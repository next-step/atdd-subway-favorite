package nextstep.auth.ui.authentication.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.domain.Authentication;
import nextstep.auth.dto.TokenResponse;
import nextstep.auth.infrastructure.JwtTokenProvider;
import nextstep.auth.ui.authentication.AuthenticationInterceptor;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor2 extends AuthenticationInterceptor {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor2(TokenAuthenticationConverter converter,
                                           CustomUserDetailsService userDetailsService,
                                           JwtTokenProvider jwtTokenProvider,
                                           ObjectMapper objectMapper) {
        super(converter, userDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String payload = objectMapper.writeValueAsString(authentication);
        String token = jwtTokenProvider.createToken(payload);

        TokenResponse tokenResponse = new TokenResponse(token);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(objectMapper.writeValueAsString(tokenResponse));
    }
}
