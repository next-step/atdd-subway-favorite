package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.PasswordCheckableUserService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final AuthenticationConverter authenticationConverter;

    public TokenAuthenticationInterceptor(PasswordCheckableUserService customUserDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        super(customUserDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.authenticationConverter = new TokenAuthTokenConverter(objectMapper);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        String jwtToken = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = new TokenResponse(jwtToken);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }
}
