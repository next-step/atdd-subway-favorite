package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final AuthenticationConverter authenticationConverter;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;


    public TokenAuthenticationInterceptor(CustomUserDetailsService userDetailsService,
        JwtTokenProvider jwtTokenProvider,
        AuthenticationConverter authenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = convert(request);
        Authentication authentication = authenticate(token);
        afterAuthentication(request, response, authentication);

        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        return authenticationConverter.convert(request);
    }

    public Authentication authenticate(AuthenticationToken token) {
        // TODO: AuthenticationToken에서 Authentication 객체 생성하기
        String principal = token.getPrincipal();
        LoginMember loginMember = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(loginMember, token);

        return new Authentication(loginMember);
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        // TODO: authentication으로 TokenResponse 추출하기
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = TokenResponse.of(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
