package nextstep.subway.auth.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.exception.NotFoundMemberException;
import nextstep.subway.auth.exception.NotMatchedPasswordException;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticatorStrategy authenticatorStrategy;

    public AuthenticationInterceptor(ObjectMapper objectMapper, CustomUserDetailsService customUserDetailsService, AuthenticatorStrategy authenticatorStrategy) {
        this.objectMapper = objectMapper;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticatorStrategy = authenticatorStrategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);

        authenticatorStrategy.authenticate(authentication, response);
        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        LoginMember member = customUserDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
        checkAuthentication(member, authenticationToken);
        return new Authentication(member);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new NotFoundMemberException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new NotMatchedPasswordException();
        }
    }

}
