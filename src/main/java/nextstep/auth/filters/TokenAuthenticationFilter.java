package nextstep.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.filters.provider.AuthenticationProvider;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.user.UserDetails;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationFilter extends AuthenticationRespondingFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationFilter(AuthenticationProvider<AuthenticationToken> authenticationProvider, JwtTokenProvider jwtTokenProvider) {
        super(authenticationProvider);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();
        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected void authenticate(UserDetails userDetails, HttpServletResponse response) throws IOException {
        String token = jwtTokenProvider.createToken(userDetails.getEmail(), userDetails.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
