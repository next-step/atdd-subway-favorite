package nextstep.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationHandler extends AuthenticationHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationHandler(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(userDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected User preAuthentication(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return findAuthMember(principal, credentials);
    }

    @Override
    protected void afterAuthentication(User user, HttpServletResponse response) throws IOException {
        String token = jwtTokenProvider.createToken(user.getPrincipal(), user.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
