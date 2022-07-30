package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationStrategy;
import nextstep.auth.service.UserDetail;
import nextstep.auth.service.UserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public class TokenAuthentication implements AuthenticationStrategy {

    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthentication(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void authenticate(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        UserDetail userDetail = userDetailsService.loadUserByUsername(principal);

        if (userDetail == null) {
            throw new AuthenticationException();
        }

        if (!userDetail.checkPassword(credentials)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetail.getEmail(), userDetail.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
