package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.convert.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.authentication.AuthenticationException.NOT_FOUND_EMAIL;
import static nextstep.auth.authentication.AuthenticationException.PASSWORD_IS_INCORRECT;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConverter tokenAuthenticationConverter;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider, AuthenticationConverter tokenAuthenticationConverter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = tokenAuthenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        afterAuthentication(request, response, authentication);

        return false;
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        LoginMember principal = customUserDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
        checkCredential(principal, authenticationToken.getCredentials());
        return new Authentication(principal);
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenResponse tokenResponse = createTokenResponse(authentication);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(convertToResponseToClient(tokenResponse));

    }

    private String convertToResponseToClient(TokenResponse tokenResponse) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(tokenResponse);
    }

    private TokenResponse createTokenResponse(Authentication authentication) throws JsonProcessingException {
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        return new TokenResponse(jwtTokenProvider.createToken(payload));
    }

    private void checkCredential(LoginMember principal, String credentials) {
        if (principal == null) {
            throw new AuthenticationException(NOT_FOUND_EMAIL);
        }
        if (!principal.checkPassword(credentials)) {
            throw new AuthenticationException(PASSWORD_IS_INCORRECT);
        }
    }
}
