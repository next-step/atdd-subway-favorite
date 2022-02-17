package nextstep.auth.authentication.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.UserDetails;
import org.springframework.http.MediaType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;;

    public TokenAuthenticationInterceptor(AuthenticationConverter authenticationConverter,
        UserDetailsService userDetailsService
            , JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        super(authenticationConverter,userDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        TokenResponse tokenResponse = new TokenResponse(jwtTokenProvider.createToken(payload));
        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
