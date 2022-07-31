package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {
    private final LoginMemberService loginMemberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TokenRequest tokenRequest = convert(request);
        LoginMember loginMember = authenticate(tokenRequest);

        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    public TokenRequest convert(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return objectMapper.readValue(content, TokenRequest.class);
    }

    public LoginMember authenticate(TokenRequest tokenRequest) {
        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        LoginMember loginMember = loginMemberService.loadUserByUsername(principal);
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(credentials)) {
            throw new AuthenticationException();
        }

        return loginMember;
    }
}
