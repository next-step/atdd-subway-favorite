package nextstep.subway.auth.ui.interceptor.authentication;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.convert.AuthenticationConverter;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private static final String REGEX = ":";

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final AuthenticationConverter authenticationConverter;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService,
        JwtTokenProvider jwtTokenProvider,
        AuthenticationConverter authenticationConverter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
        IOException {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = Optional.of(authenticate(authenticationToken))
            .orElseThrow(() -> new RuntimeException("authentication is null"));
        String jwtToken = jwtTokenProvider.createToken(authenticationToken.getPrincipal());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new TokenResponse(jwtToken)));
        return false;
    }

    private Authentication authenticate(AuthenticationToken token) {
        LoginMember loginMember = customUserDetailsService.loadUserByUsername(token.getPrincipal());
        if (Objects.isNull(loginMember)) {
            throw new RuntimeException("there is no user.");
        }
        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new RuntimeException("password is wrong.");
        }
        return new Authentication(loginMember);
    }
}
