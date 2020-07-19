package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private static final String CREDENTIAL_DELIMITER = ":";

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public TokenAuthenticationInterceptor(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = convertToken(request);

        Authentication authentication = authenticate(authenticationToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);

        TokenResponse tokenResponse = new TokenResponse(token);
        objectMapper.writeValue(response.getOutputStream(), tokenResponse);

        return false;
    }

    public AuthenticationToken convertToken(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String decodedToken = new String(Base64.decodeBase64(token.getBytes()));
        int delimiterIndex = decodedToken.indexOf(CREDENTIAL_DELIMITER);

        if (delimiterIndex < 0) {
            throw new IllegalArgumentException("invalid token string");
        }

        String principal = decodedToken.substring(0, delimiterIndex);
        String credentials = decodedToken.substring(delimiterIndex + 1);

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken token) {
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
