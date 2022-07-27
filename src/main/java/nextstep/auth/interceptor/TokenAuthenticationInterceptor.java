package nextstep.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthMemberLoader;
import nextstep.auth.context.Authentication;
import nextstep.auth.converter.AuthenticationConverter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor{
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(AuthenticationConverter authenticationConverter, AuthMemberLoader authMemberLoader, JwtTokenProvider jwtTokenProvider) {
        super(authenticationConverter, authMemberLoader);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = jwtTokenProvider.createToken((String) authentication.getPrincipal(), authentication.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
