package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {
    private JwtTokenProvider jwtTokenProvider;
    private TokenAuthenticationConverter tokenAuthenticationConverter;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(customUserDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenAuthenticationConverter = new TokenAuthenticationConverter();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = tokenAuthenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);

        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }
}
