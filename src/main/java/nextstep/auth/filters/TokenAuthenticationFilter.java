package nextstep.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.filters.converter.AuthenticationConverter;
import nextstep.auth.filters.provider.AuthenticationProvider;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.user.UserDetails;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends AuthenticationRespondingFilter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConverter authenticationConverter;

    public TokenAuthenticationFilter(AuthenticationProvider<AuthenticationToken> authenticationProvider,
                                     JwtTokenProvider jwtTokenProvider,
                                     AuthenticationConverter authenticationConverter) {

        super(authenticationProvider);
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) throws IOException {
        return authenticationConverter.convert(request);
    }

    @Override
    protected void authenticate(UserDetails userDetails, HttpServletResponse response) throws IOException {
        String token = jwtTokenProvider.createToken(userDetails.getPrincipal(), userDetails.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = OBJECT_MAPPER.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
