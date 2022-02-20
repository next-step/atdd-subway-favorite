package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;
    private AuthenticationConverter authenticationConverter;
    private UserAuthentication userAuthentication;


    public TokenAuthenticationInterceptor(JwtTokenProvider jwtTokenProvider,
                                          ObjectMapper objectMapper,
                                          AuthenticationConverter authenticationConverter,
                                          UserAuthentication userAuthentication) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.authenticationConverter = authenticationConverter;
        this.userAuthentication = userAuthentication;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = userAuthentication.authenticate(authenticationToken);

        final String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        final String token = jwtTokenProvider.createToken(payload);

        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

}
