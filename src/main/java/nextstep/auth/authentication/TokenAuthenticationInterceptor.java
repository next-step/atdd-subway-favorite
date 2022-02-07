package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.convert.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.service.UserDetailsService;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider, AuthenticationConverter tokenAuthenticationConverter) {
        super(customUserDetailsService, tokenAuthenticationConverter);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenResponse tokenResponse = createTokenResponse(authentication);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(convertToResponseToClient(tokenResponse));

    }

    private String convertToResponseToClient(TokenResponse tokenResponse) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(tokenResponse);
    }

    private TokenResponse createTokenResponse(Authentication authentication) throws JsonProcessingException {
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        return new TokenResponse(jwtTokenProvider.createToken(payload));
    }

}
