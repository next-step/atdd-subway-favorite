package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);

        // TODO: authentication으로 TokenResponse 추출하기
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = new TokenResponse(token);
        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        // TODO: request에서 AuthenticationToken 객체 생성하기
        ObjectMapper objectMapper = new ObjectMapper();
        TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        // TODO: AuthenticationToken에서 AuthenticationToken 객체 생성하기
        LoginMember loginMember = customUserDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
        checkAuthentication(loginMember, authenticationToken);
        return new Authentication(loginMember);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
