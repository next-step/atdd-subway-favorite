package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.common.util.ServletUtils;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(CustomUserDetailsService userDetailsService,
                                          JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        super(userDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);

        TokenResponse tokenResponse = tokenResponse(authentication);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = ServletUtils.readJson(request, TokenRequest.class);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    private TokenResponse tokenResponse(Authentication authentication) throws JsonProcessingException {
        String jwtToken = createJwtToken(authentication);
        return new TokenResponse(jwtToken);
    }

    private String createJwtToken(Authentication authentication) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        return jwtTokenProvider.createToken(payload);
    }
}
