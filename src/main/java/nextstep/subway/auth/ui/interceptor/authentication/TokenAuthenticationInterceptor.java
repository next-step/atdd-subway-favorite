package nextstep.subway.auth.ui.interceptor.authentication;

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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {
    
    private static final String AUTHORIZATION_DELIMITER = ":";

    private ObjectMapper objectMapper;
    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetailsService userDetailsService;

    public TokenAuthenticationInterceptor(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.objectMapper = new ObjectMapper();
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken token = convert(request);
        Authentication authentication = authenticate(token);
        TokenResponse tokenResponse = createTokenResponse(authentication);
        respond(response, tokenResponse);
        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        String authorization = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);

        byte[] decodedBytes = Base64.getDecoder().decode(authorization.getBytes());
        String decodedAuthorization = new String(decodedBytes);
        String[] split = decodedAuthorization.split(AUTHORIZATION_DELIMITER);

        String principal = split[0];
        String credential = split[1];

        return new AuthenticationToken(principal, credential);
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

    private TokenResponse createTokenResponse(Authentication authentication) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        return new TokenResponse(token);
    }

    private void respond(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(tokenResponse));
        }
    }
}
