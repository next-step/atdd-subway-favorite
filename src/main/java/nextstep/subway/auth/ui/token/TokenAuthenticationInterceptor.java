package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationAfterCompletion;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.TokenAuthenticate;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TokenAuthenticationInterceptor extends AuthenticationAfterCompletion implements HandlerInterceptor  {

    private TokenAuthenticate tokenAuthenticate;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationConverter authenticationConverter;

    public TokenAuthenticationInterceptor(TokenAuthenticate tokenAuthenticate, JwtTokenProvider jwtTokenProvider) {
        this.tokenAuthenticate = tokenAuthenticate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationConverter = new TokenAuthenticationConverter();
    }

    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        TokenResponse tokenResponse = new TokenResponse(generateJWToken(authentication));
        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = tokenAuthenticate.authenticate(authenticationToken);
        if (authentication == null) {
            throw new RuntimeException();
        }
        afterAuthentication(request, response, authentication);
        return false;
    }

    private String generateJWToken(Authentication authentication) throws JsonProcessingException {
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtTokenProvider.createToken(new ObjectMapper().writeValueAsString(userDetails));
    }
}
