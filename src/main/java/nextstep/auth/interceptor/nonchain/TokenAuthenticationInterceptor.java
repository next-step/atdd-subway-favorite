package nextstep.auth.interceptor.nonchain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.springframework.http.MediaType;

public class TokenAuthenticationInterceptor extends AuthNonChainInterceptor {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected UserDetails createAuthentication(final HttpServletRequest request) {
        try {
            String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

            String principal = tokenRequest.getEmail();
            String credentials = tokenRequest.getPassword();

            UserDetails loginMember = userDetailsService.loadUserByUsername(principal);

            if (loginMember == null) {
                throw new AuthenticationException();
            }

            if (!loginMember.checkPassword(credentials)) {
                throw new AuthenticationException();
            }
            return loginMember;
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    @Override
    protected void afterHandle(final UserDetails loginMember, final HttpServletResponse response) {
        try {
            String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
            TokenResponse tokenResponse = new TokenResponse(token);

            String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().print(responseToClient);
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

}
