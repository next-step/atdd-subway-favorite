package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailServiceStrategy;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.application.UserDetailsServiceStrategy;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;
    private AuthenticationConverter authenticationConverter;


    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService,
                                          JwtTokenProvider jwtTokenProvider,
                                          ObjectMapper objectMapper,
                                          AuthenticationConverter authenticationConverter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken, new CustomUserDetailServiceStrategy(customUserDetailsService));

        final String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
        final String token = jwtTokenProvider.createToken(payload);

        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    public Authentication authenticate(AuthenticationToken token, UserDetailsServiceStrategy userDetailsService) {
        String principal = token.getPrincipal();
        final LoginMember userDetails = userDetailsService.authenticate(principal, token);
        return new Authentication(userDetails);
    }
}
