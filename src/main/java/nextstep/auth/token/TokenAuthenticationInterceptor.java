package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.LoginFilter;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends LoginFilter {
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        super(loginMemberService);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        LoginMember loginMember = loginMemberService.loadUserByUsername(principal);

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(credentials)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
