package nextstep.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends AuthenticationNonChainHandler {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String content = null;
        try {
            content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

            return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    @Override
    protected UserDetails getUserDetails(AuthenticationToken authenticationToken) {
            return userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
    }

    @Override
    protected void afterHandle(UserDetails userDetails, HttpServletResponse response) {
        String token = jwtTokenProvider.createToken(userDetails.getEmail(), userDetails.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);
        String responseToClient = null;

        try {
            responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().print(responseToClient);
        } catch (Exception e) {
            throw new AuthenticationException();
        }

    }
}
