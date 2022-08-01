package nextstep.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.user.UserDetailsService;
import nextstep.member.domain.LoginMember;
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
    protected LoginMember createAuthentication(HttpServletRequest request) {
        try {
            String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

            String principal = tokenRequest.getEmail();
            String credentials = tokenRequest.getPassword();

            LoginMember loginMember = userDetailsService.loadUserByUsername(principal);

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
    protected void afterHandle(LoginMember loginMember, HttpServletResponse response) {
        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
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
