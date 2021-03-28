package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.io.IOException;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 요청으로 들어온 인증정보 확인
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);
        // 토큰 생성및 응답객체에 세팅
        TokenResponse tokenResponse = getTokenResponse(authentication);
        setTokenResponse(response, tokenResponse);
        return false;
    }

    private void setTokenResponse(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    private TokenResponse getTokenResponse(Authentication authentication) throws JsonProcessingException {
        // Authentication -> TokenResponse
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = new TokenResponse(token);
        return tokenResponse;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        // request -> AuthenticationToken
        TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);

        String principal = tokenRequest.getEmail(); // 본인
        String credentials = tokenRequest.getPassword(); // 자격

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        // AuthenticationToken -> Authentication
        LoginMember loginMember = customUserDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

        if(loginMember == null){
            new RuntimeException("존재하지 않는 이메일 입니다.");
        }

        if(!loginMember.checkPassword(authenticationToken.getCredentials())){
            new RuntimeException("비밀번호가 유효하지 않습니다.");
        }

        return new Authentication(loginMember);
    }
}
