package nextstep.auth.authentication.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.domain.member.service.UserDetailsService;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor implements HandlerInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider ) {
        super(userDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request)  {
        TokenRequest body = null;
        try {
            body = requestToToken(request);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        String principal = body.getEmail();
        String credentials = body.getPassword();
        return new AuthenticationToken(principal, credentials);
    }

    private String convertToJson(TokenResponse tokenResponse) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(tokenResponse);
    }

    private TokenResponse createTokenResponse(Authentication authentication) throws JsonProcessingException {
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        return new TokenResponse(token);
    }

    private TokenRequest requestToToken(HttpServletRequest request) throws IOException {
        return new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenResponse tokenResponse = createTokenResponse(authentication);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(convertToJson(tokenResponse));
    }
}
