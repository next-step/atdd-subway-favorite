package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.exception.InvalidPasswordException;
import nextstep.subway.exception.UserDetailsNotExistException;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    public static final String EMAIL_FIELD = "email";
    public static final String PASSWORD_FIELD = "password";

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper mapper;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);
        TokenResponse tokenResponse = create(authentication);
        send(response, tokenResponse);
        return false;
    }

    private void send(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    private TokenResponse create(Authentication authentication) throws JsonProcessingException {
        String payload = mapper.writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = new TokenResponse(token);
        return tokenResponse;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String body = CharStreams.toString(request.getReader());
        Map<String, String> map = mapper.readValue(body, Map.class);

        String principal = map.get(EMAIL_FIELD);
        String credentials = map.get(PASSWORD_FIELD);

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        LoginMember userDetails = customUserDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new UserDetailsNotExistException();
        }
        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new InvalidPasswordException();
        }
    }
}
