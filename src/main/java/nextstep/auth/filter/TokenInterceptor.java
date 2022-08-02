package nextstep.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TokenInterceptor extends AuthenticationFilter {
    private final JwtTokenProvider provider;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TokenInterceptor(LoginService loginService, JwtTokenProvider provider) {
        super(loginService);
        this.provider = provider;
    }

    @Override
    protected Authentication getAuthentication(HttpServletRequest request) {
        TokenRequest tokenRequest = getTokenRequest(request);
        String email = isNotNullAndNotEmpty(tokenRequest.getEmail());
        String password = isNotNullAndNotEmpty(tokenRequest.getPassword());
        return new Authentication(email, password);
    }


    @Override
    protected void execute(HttpServletResponse response, String email, List<String> authorities) throws IOException {
        String token = provider.createToken(email, authorities);
        TokenResponse tokenResponse = new TokenResponse(token);
        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    private TokenRequest getTokenRequest(HttpServletRequest request) {
        try {
            String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            return objectMapper.readValue(content, TokenRequest.class);
        } catch (IOException e) {
            throw new AuthenticationException();
        }
    }

}
