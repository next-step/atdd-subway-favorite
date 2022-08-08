package nextstep.auth.authentication.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.user.UserDetails;
import nextstep.auth.authentication.user.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

public class TokenAuthenticationFilter extends AuthenticationNotChainingFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    AuthenticationToken convert(HttpServletRequest request) throws Exception {
        String content = request.getReader().lines()
            .collect(Collectors.joining(System.lineSeparator()));

        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    @Override
    UserDetails createUserDetails(AuthenticationToken token) {
        return userDetailsService.loadUserByUsername(token.getPrincipal());
    }

    @Override
    void afterAuthentication(Authentication authenticate, HttpServletResponse response) throws Exception {
        String token = jwtTokenProvider.createToken(String.valueOf(authenticate.getPrincipal()), authenticate.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
