package nextstep.auth.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(customUserDetailsService, new TokenAuthenticationConverter());
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            TokenResponse tokenResponse = getToken(authentication);
            String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().print(responseToClient);
        } catch (IOException e) {
            throw new AuthenticationException();
        }
    }

    private TokenResponse getToken(Authentication authentication) throws JsonProcessingException {
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);

        return new TokenResponse(token);
    }
}
