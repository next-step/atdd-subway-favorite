package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        super(customUserDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected void afterAuthentication(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException {
        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
