package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import nextstep.auth.authentication.MemberInfo;
import nextstep.auth.authentication.NonChainFilter;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends NonChainFilter {
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        super(loginMemberService);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @SneakyThrows
    @Override
    protected MemberInfo createPrincipal(HttpServletRequest request) {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String email = tokenRequest.getEmail();
        String password = tokenRequest.getPassword();

        return new MemberInfo(email, password);
    }

    @Override
    protected void afterValidation(HttpServletResponse response, LoginMember loginMember) throws IOException {
        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);
        createResponse(response, tokenResponse);
    }

    private void createResponse(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
