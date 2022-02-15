package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailServiceStrategy;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor implements HandlerInterceptor, AuthenticationConverter {

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;


    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService,
                                          JwtTokenProvider jwtTokenProvider,
                                          ObjectMapper objectMapper) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken, new CustomUserDetailServiceStrategy(customUserDetailsService));

        final String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        final String token = jwtTokenProvider.createToken(payload);

        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        final TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }
}
