package nextstep.auth.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends AuthenticationNonChainHandler {
    private LoginMemberService loginMemberService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected LoginMember createAuthentication(HttpServletRequest request) {
        try {
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

            return loginMember;
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    @Override
    protected void afterHandle(LoginMember loginMember, HttpServletResponse response) {
        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = null;
        try {
            responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().print(responseToClient);
        } catch (Exception e) {
            throw new AuthenticationException();
        }

    }
}
