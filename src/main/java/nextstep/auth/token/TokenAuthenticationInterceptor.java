package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationNonChainingFilter;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends AuthenticationNonChainingFilter {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private TokenRequest getTokenRequest(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);
        return tokenRequest;
    }

    @Override
    protected UserDetails convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = getTokenRequest(request);
        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);

        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(credentials)) {
            throw new AuthenticationException();
        }
        return userDetails;
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
