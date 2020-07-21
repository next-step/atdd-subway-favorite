package nextstep.subway.auth.ui.interceptor.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Objects;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {
    private static final String REGEX = ":";

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);

        if (authentication == null) {
            throw new RuntimeException();
        }

        String token = jwtTokenProvider.createToken(authenticationToken.getPrincipal());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new TokenResponse(token)));
        return false;
    }

    private AuthenticationToken convert(HttpServletRequest request) throws JsonProcessingException {
        String result = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);

        byte[] decodedBytes = Base64.getDecoder().decode(result);
        String enCodeString = new String(decodedBytes);
        String principal = "";
        String credentials = "";
        if (Objects.nonNull(enCodeString)) {
            String[] split = enCodeString.split(REGEX);
            principal = split[0];
            credentials = split[1];
        }

        return new AuthenticationToken(principal, credentials);
    }

    private Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }


    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
