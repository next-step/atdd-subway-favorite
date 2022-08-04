package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor2 implements HandlerInterceptor {
    private final LoginMemberService loginMemberService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor2(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = convert(request);
        LoginMember loginMember = validate(authenticationToken);
        authenticate(loginMember, response);
        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();
        return new AuthenticationToken(principal, credentials);
    }

    public LoginMember validate(AuthenticationToken token) {
        LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());
        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return loginMember;
    }

    public void authenticate(LoginMember loginMember, HttpServletResponse response) throws IOException {
        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

}
