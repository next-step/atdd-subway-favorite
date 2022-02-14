package nextstep.auth.authentication.token;


import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.UserDetailService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(
        UserDetailService customUserDetailsService,
        TokenAuthenticationConverter tokenAuthenticationConverter,
        JwtTokenProvider jwtTokenProvider,
        ObjectMapper objectMapper
    ) {
        super(customUserDetailsService, tokenAuthenticationConverter);
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        TokenResponse tokenResponse = new TokenResponse(jwtTokenProvider.createToken(payload));

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
