package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.user.User;
import nextstep.auth.interceptor.AuthNotChainInterceptor;
import nextstep.auth.user.UserDetail;
import nextstep.auth.user.UserDetailService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthInterceptor extends AuthNotChainInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthInterceptor(final UserDetailService userDetailService, final JwtTokenProvider jwtTokenProvider) {
        super(userDetailService);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected AuthenticationToken createAuthToken(final HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    @Override
    protected void afterSuccessUserCheck(final HttpServletRequest request,
                                         final HttpServletResponse response,
                                         final UserDetail user) throws IOException {
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
