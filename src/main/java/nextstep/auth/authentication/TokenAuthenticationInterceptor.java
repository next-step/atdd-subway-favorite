package nextstep.auth.authentication;

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
import java.util.Objects;

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

        // TODO: authentication으로 TokenResponse 추출하기
        TokenResponse tokenResponse = null;

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    public AuthenticationToken convert(final HttpServletRequest request) throws IOException {
        final TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);
        final String principal = tokenRequest.getEmail();
        final String credentials = tokenRequest.getPassword();
        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(final AuthenticationToken authenticationToken) {
        return new Authentication(null);
    }

    private void checkAuthentication(final LoginMember userDetails, final AuthenticationToken token) {
        if (Objects.isNull(userDetails)) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
