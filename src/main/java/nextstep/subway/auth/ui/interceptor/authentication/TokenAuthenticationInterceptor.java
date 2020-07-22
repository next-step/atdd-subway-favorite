package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import nextstep.subway.auth.application.AuthenticationProvider;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.utils.ObjectMapperUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

    private static final String CREDENTIAL_DELIMITER = ":";

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(AuthenticationProvider authenticationProvider, JwtTokenProvider jwtTokenProvider) {
        super(authenticationProvider, AuthenticationTokenExtractor.of(AuthenticationTokenExtractor.Type.BASIC));
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void applyAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenResponse tokenResponse = obtainAuthenticationTokenResponse(authentication);
        writeTokenResponse(response, tokenResponse);
    }

    private void writeTokenResponse(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapperUtils.writeStream(response.getOutputStream(), tokenResponse);
    }

    private TokenResponse obtainAuthenticationTokenResponse(Authentication authentication) throws JsonProcessingException {
        String payload = ObjectMapperUtils.convertAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);

        return new TokenResponse(token);
    }
}
