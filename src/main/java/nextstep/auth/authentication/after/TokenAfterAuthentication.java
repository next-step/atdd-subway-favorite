package nextstep.auth.authentication.after;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.after.AfterAuthentication;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;

@RequiredArgsConstructor
public class TokenAfterAuthentication implements AfterAuthentication {
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
                                       Authentication authentication) throws IOException {
        TokenResponse tokenResponse = tokenResponse(authentication);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    private TokenResponse tokenResponse(Authentication authentication) throws JsonProcessingException {
        String jwtToken = createJwtToken(authentication);
        return new TokenResponse(jwtToken);
    }

    private String createJwtToken(Authentication authentication) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        return jwtTokenProvider.createToken(payload);
    }
}
