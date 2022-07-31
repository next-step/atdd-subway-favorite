package nextstep.auth.authentication.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends NonChainingAuthenticationInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider, final ObjectMapper objectMapper) {
        super(loginMemberService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    AuthenticateRequest createLoginRequest(final HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return new ObjectMapper().readValue(content, AuthenticateRequest.class);
    }

    void afterAuthenticate(final HttpServletResponse response, final LoginMember loginMember) throws IOException {
        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

}
