package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationNonChainInterceptor;
import nextstep.auth.authentication.UserInformation;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

public class TokenAuthenticationInterceptor extends AuthenticationNonChainInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        super(loginMemberService);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void afterAuthentication(HttpServletResponse response, LoginMember loginMember) throws IOException {
        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    @Override
    protected UserInformation createPrincipal(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String email = tokenRequest.getEmail();
        String password = tokenRequest.getPassword();

        return new UserInformation(email, password);
    }
}
