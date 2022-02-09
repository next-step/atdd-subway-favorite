package nextstep.auth.authentication.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AbstractAuthenticationInterceptor;
import nextstep.auth.authentication.ProviderManager;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper, TokenAuthenticationConverter converter, ProviderManager providerManager) {
        super(converter, providerManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
