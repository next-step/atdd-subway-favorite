package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailService customUserDetailsService,
                                          AuthenticationConverter authenticationConverter,
                                          JwtTokenProvider jwtTokenProvider) {
        super(customUserDetailsService, authenticationConverter);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = token(authentication);
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    private String token(Authentication authentication) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(authentication.getPrincipal());

        return jwtTokenProvider.createToken(payload);
    }

}
