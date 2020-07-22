package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.utils.ObjectMapperUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

    private static final String CREDENTIAL_DELIMITER = ":";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(userDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void applyAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO 이 메소드를 추상화 할 수는 없을까 with session auth interceptor
        AuthenticationToken authenticationToken = convertToken(request);

        Authentication authentication = authenticate(authenticationToken);

        TokenResponse tokenResponse = obtainAuthenticationTokenResponse(authentication);

        writeTokenResponse(response, tokenResponse);
    }

    private void writeTokenResponse(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        ObjectMapperUtils.writeStream(response.getOutputStream(), tokenResponse);
    }

    private TokenResponse obtainAuthenticationTokenResponse(Authentication authentication) throws JsonProcessingException {
        String payload = ObjectMapperUtils.convertAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);

        return new TokenResponse(token);
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
}
