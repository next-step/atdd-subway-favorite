package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.converter.AuthenticationConverter;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {
    
    private ObjectMapper objectMapper;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(JwtTokenProvider jwtTokenProvider,
                                          UserDetailsService userDetailsService,
                                          AuthenticationConverter authenticationConverter) {
        super(userDetailsService, authenticationConverter);
        this.objectMapper = new ObjectMapper();
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenResponse tokenResponse = createTokenResponse(authentication);
        respond(response, tokenResponse);
    }

    private TokenResponse createTokenResponse(Authentication authentication) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        return new TokenResponse(token);
    }

    private void respond(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(tokenResponse));
        }
    }
}
