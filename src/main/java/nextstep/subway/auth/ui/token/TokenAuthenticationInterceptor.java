package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.TokenAuthenticate;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private TokenAuthenticate tokenAuthenticate;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationConverter authenticationConverter;

    public TokenAuthenticationInterceptor(TokenAuthenticate tokenAuthenticate, JwtTokenProvider jwtTokenProvider) {
        this.tokenAuthenticate = tokenAuthenticate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationConverter = new TokenAuthenticationConverter();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = tokenAuthenticate.authenticate(authenticationToken);

        TokenResponse tokenResponse = new TokenResponse(generateJWToken(authentication));
        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
        return false;
    }

    private String generateJWToken(Authentication authentication){
        LoginMember userDetails = (LoginMember) authentication.getPrincipal();
        return jwtTokenProvider.createToken(convertMember(userDetails));
    }

    private String convertMember(LoginMember member){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(member);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
