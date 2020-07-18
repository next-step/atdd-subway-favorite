package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.AuthenticationException;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Objects;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    public static final String REGEX = ":";
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = convert(request);

        Authentication authenticate = authenticate(authenticationToken);

        String token = getToken(authenticationToken);
        TokenResponse tokenResponse = new TokenResponse(token);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
        response.flushBuffer();


        return false;
    }

    private String getToken(AuthenticationToken authenticationToken) {
        return jwtTokenProvider.createToken(authenticationToken.getPrincipal());
    }

    private Authentication authenticate(AuthenticationToken authenticationToken) {
        LoginMember loginMember = customUserDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

        if (Objects.isNull(loginMember)) {
            throw new AuthenticationException();
        }
        if (!loginMember.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(loginMember);
    }

    private AuthenticationToken convert(HttpServletRequest request) {
        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);

        byte[] decodeBytes = Base64.getDecoder().decode(credentials);
        String decodedCredentials = new String(decodeBytes);
        String[] split = decodedCredentials.split(REGEX);
        String username = split[0];
        String password = split[1];

        return new AuthenticationToken(username, password);
    }
}
