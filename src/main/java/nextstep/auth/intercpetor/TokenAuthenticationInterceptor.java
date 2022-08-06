package nextstep.auth.intercpetor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends NonChainFilter {
    private LoginMemberService loginMemberService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws Exception {
        String content = request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        String credentials = token.getCredentials();

        LoginMember loginMember = loginMemberService.loadUserByUsername(principal);

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(credentials)) {
            throw new AuthenticationException();
        }

        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }

    @Override
    public void afterAuthenticated(Authentication authentication, HttpServletResponse response) throws JsonProcessingException, Exception {
        String token = jwtTokenProvider.createToken(authentication.getPrincipal()
                .toString(), authentication.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream()
                .print(responseToClient);
    }
}
