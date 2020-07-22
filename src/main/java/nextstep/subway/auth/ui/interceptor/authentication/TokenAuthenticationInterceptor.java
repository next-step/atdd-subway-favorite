package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws JsonProcessingException {
        AuthenticationToken token = convert(request);
        Authentication authentication = authenticate(token);

        if (authentication == null) {
            throw new RuntimeException();
        }

        final String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        final String jwtToken = this.jwtTokenProvider.createToken(payload);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (PrintWriter writer = response.getWriter()) {
            TokenResponse tokenResponse = new TokenResponse(jwtToken);
            writer.write(new ObjectMapper().writeValueAsString(tokenResponse));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        String payload = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String[] emailAndPassword = new String(Base64.getDecoder().decode(payload)).split(":");

        return new AuthenticationToken(emailAndPassword[0], emailAndPassword[1]);
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
