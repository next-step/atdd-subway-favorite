package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.domain.member.service.LegacyUserDetailsService;
import nextstep.domain.member.domain.LoginMember;
import nextstep.domain.member.service.UserDetailsService;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);

        TokenResponse tokenResponse = createTokenResponse(authentication);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(convertToJson(tokenResponse));
        return false;
    }



    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest body = reqeustToToken(request);
        String principal = body.getEmail();
        String credentials = body.getPassword();
        return new AuthenticationToken(principal, credentials);
    }


    public Authentication authenticate(AuthenticationToken authenticationToken) {
        LoginMember loginMember = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
        loginMember.checkPassword(authenticationToken.getCredentials());
        return new Authentication(loginMember);
    }

    private String convertToJson(TokenResponse tokenResponse) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(tokenResponse);
    }

    private TokenResponse createTokenResponse(Authentication authentication) throws JsonProcessingException {
        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        return new TokenResponse(token);
    }

    private TokenRequest reqeustToToken(HttpServletRequest request) throws IOException {
        return new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);
    }

}
