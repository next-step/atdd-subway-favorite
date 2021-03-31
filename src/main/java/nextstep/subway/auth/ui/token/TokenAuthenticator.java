package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticatorStrategy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Primary
public class TokenAuthenticator implements AuthenticatorStrategy {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticator(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper){
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void authenticate(Authentication authentication, HttpServletResponse response) throws IOException {
        String accessToken = createAccessToken(authentication);
        TokenResponse tokenResponse = new TokenResponse(accessToken);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    private String createAccessToken(Authentication authentication) throws JsonProcessingException {
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        return jwtTokenProvider.createToken(payload);
    }
}
