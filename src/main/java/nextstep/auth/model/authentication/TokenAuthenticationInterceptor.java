package nextstep.auth.model.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.authentication.service.CustomUserDetailsService;
import nextstep.auth.model.context.Authentication;
import nextstep.auth.model.token.JwtTokenProvider;
import nextstep.auth.model.token.dto.TokenResponse;
import nextstep.subway.domain.member.MemberAdaptor;
import nextstep.utils.exception.AuthenticationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(value = "tokenAuthenticationInterceptor")
public class TokenAuthenticationInterceptor implements AuthenticationInterceptor {

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService,
                                          JwtTokenProvider jwtTokenProvider,
                                          ObjectMapper objectMapper) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getInputStream(), AuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(AuthenticationToken authenticationToken) {
        MemberAdaptor memberAdaptor = customUserDetailsService.loadUserByUsername(authenticationToken.getEmail());
        validatePassword(memberAdaptor, authenticationToken);

        return new Authentication(memberAdaptor);
    }

    private void validatePassword(MemberAdaptor memberAdaptor, AuthenticationToken authenticationToken) {
        if (memberAdaptor.checkPassword(authenticationToken.getPassword())) {
            memberAdaptor.clearPassword();
            return;
        }
        throw new AuthenticationException();
    }

    @Override
    public void afterAuthenticate(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenResponse tokenResponse = TokenResponse.from(createJwtToken(authentication));
        makeResponse(response, tokenResponse);
    }

    private String createJwtToken(Authentication authentication) {
        MemberAdaptor memberAdaptor = (MemberAdaptor) authentication.getPrincipal();
        return jwtTokenProvider.createToken(memberAdaptor.getEmail());
    }

    private void makeResponse(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
