package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAfterAuthentication implements AfterAuthentication {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAfterAuthentication(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doAfter(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String loginMember = objectMapper.writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(loginMember);
        TokenResponse tokenResponse = new TokenResponse(token);
        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
