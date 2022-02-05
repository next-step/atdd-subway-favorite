package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.authentication.AuthenticationException.NOT_FOUND_EMAIL;
import static nextstep.auth.authentication.AuthenticationException.PASSWORD_IS_INCORRECT;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);

        TokenResponse tokenResponse = createTokenResponse(authentication);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(convertToResponseToClient(tokenResponse));

        return false;
    }

    private String convertToResponseToClient(TokenResponse tokenResponse) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(tokenResponse);
    }

    private TokenResponse createTokenResponse(Authentication authentication) throws JsonProcessingException {
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        return new TokenResponse(jwtTokenProvider.createToken(payload));
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = readTokenRequest(request);
        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        LoginMember principal = customUserDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
        checkCredential(principal, authenticationToken.getCredentials());
        return new Authentication(principal);
    }

    private void checkCredential(LoginMember principal, String credentials) {
        if (principal == null) {
            throw new AuthenticationException(NOT_FOUND_EMAIL);
        }
        if (!principal.checkPassword(credentials)) {
            throw new AuthenticationException(PASSWORD_IS_INCORRECT);
        }
    }

    private TokenRequest readTokenRequest(HttpServletRequest request) throws IOException {
        return new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);
    }
}
