package nextstep.auth.authentication.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationConverter authenticationConverter;
    private ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService,
                                          JwtTokenProvider jwtTokenProvider,
                                          AuthenticationConverter authenticationConverter, ObjectMapper objectMapper) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationConverter = authenticationConverter;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        String accessToken = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = new TokenResponse(accessToken);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        LoginMember userDetails = customUserDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken authenticationToken) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
