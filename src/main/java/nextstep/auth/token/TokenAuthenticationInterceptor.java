package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.interceptor.NonChainFilter;
import nextstep.member.user.User;
import nextstep.member.user.UserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends NonChainFilter {

    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        super(userDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public User preAuthentication(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = objectMapper.readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return findAuthMember(principal, credentials);
    }

    @Override
    public void afterAuthentication(User user, HttpServletResponse response) throws IOException {
        String token = jwtTokenProvider.createToken(user.getPrincipal(), user.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

    }

}
