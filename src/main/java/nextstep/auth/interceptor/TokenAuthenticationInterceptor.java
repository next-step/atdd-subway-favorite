package nextstep.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.springframework.http.MediaType;

@RequiredArgsConstructor
public class TokenAuthenticationInterceptor extends AuthenticationNonChainHandler {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        try {
            String content = request.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
            TokenRequest tokenRequest = objectMapper.readValue(content, TokenRequest.class);

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
        String token = jwtTokenProvider.createToken(userDetails.getEmail(),
            userDetails.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        try {
            String responseToClient = objectMapper.writeValueAsString(tokenResponse);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().print(responseToClient);
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}
