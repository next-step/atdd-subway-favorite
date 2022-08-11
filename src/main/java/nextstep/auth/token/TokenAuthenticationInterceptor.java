package nextstep.auth.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.UserDetailsService;
import nextstep.auth.authentication.AbstractCreateAuthenticationFilter;
import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends AbstractCreateAuthenticationFilter {
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(userDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);
        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();
        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected String returnAuthenticationToken(String principal, List<String> authorities) throws JsonProcessingException {
        String token = jwtTokenProvider.createToken(principal, authorities);
        TokenResponse tokenResponse = new TokenResponse(token);
        return new ObjectMapper().writeValueAsString(tokenResponse);
    }

}
