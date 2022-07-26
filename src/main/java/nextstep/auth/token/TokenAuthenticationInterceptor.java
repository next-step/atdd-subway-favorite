package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthMember;
import nextstep.auth.authentication.AuthMemberLoader;
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
    private AuthMemberLoader authMemberLoader;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(AuthMemberLoader authMemberLoader, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.authMemberLoader = authMemberLoader;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TokenRequest tokenRequest = convert(request);
        AuthMember authMember = authenticate(tokenRequest);

        String token = jwtTokenProvider.createToken(authMember.getEmail(), authMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    public AuthMember authenticate(TokenRequest tokenRequest) {
        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();
        AuthMember authMember = authMemberLoader.loadUserByUsername(principal);

        validation(credentials, authMember);
        return authMember;
    }

    public TokenRequest convert(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return objectMapper.readValue(content, TokenRequest.class);
    }

    private void validation(String credentials, AuthMember authMember) {
        if (authMember == null) {
            throw new AuthenticationException();
        }

        if (!authMember.checkPassword(credentials)) {
            throw new AuthenticationException();
        }
    }
}
