package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.UserDetail;
import nextstep.subway.auth.domain.UserDetailService;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.AuthenticationInterceptor;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(UserDetailService userDetailService, AuthenticationConverter authenticationConverter, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        super(userDetailService, authenticationConverter);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenResponse tokenResponse = getTokenResponse(authentication);
        updateResponse(response, tokenResponse);
    }

    private TokenResponse getTokenResponse(Authentication authentication) throws JsonProcessingException {
        UserDetail userDetails = (UserDetail) authentication.getPrincipal();
        String token = jwtTokenProvider.createToken(objectMapper.writeValueAsString(userDetails));
        return new TokenResponse(token);
    }

    private void updateResponse(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
