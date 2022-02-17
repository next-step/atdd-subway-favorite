package nextstep.auth.authentication.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.convertor.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper = new ObjectMapper();

    public TokenAuthenticationInterceptor(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, AuthenticationConverter converter) {
        super(userDetailsService, converter);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        TokenResponse tokenResponse = new TokenResponse(jwtTokenProvider.createToken(payload));

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

}
