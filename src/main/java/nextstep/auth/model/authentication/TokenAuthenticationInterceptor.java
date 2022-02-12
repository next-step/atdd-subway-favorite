package nextstep.auth.model.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.authentication.service.CustomUserDetailsService;
import nextstep.auth.model.context.Authentication;
import nextstep.auth.model.context.SecurityContext;
import nextstep.auth.model.token.JwtTokenProvider;
import nextstep.auth.model.token.dto.TokenResponse;
import nextstep.subway.domain.member.MemberAdaptor;
import nextstep.utils.exception.AuthenticationException;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.model.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = extractAuthenticationToken(request);
        Authentication authentication = authenticate(authenticationToken);

        // TODO: authentication으로 TokenResponse 추출하기'
        String token = extractJwtToken(authentication);
        TokenResponse tokenResponse = null;

        pushSecurityContextInSession(request, new SecurityContext(authentication));
        makeResponse(response, tokenResponse);

        return false;
    }

    private void makeResponse(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

    public AuthenticationToken extractAuthenticationToken(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getInputStream(), AuthenticationToken.class);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        MemberAdaptor memberAdaptor = customUserDetailsService.loadUserByUsername(authenticationToken.getEmail());
        validatePassword(memberAdaptor, authenticationToken);

        return new Authentication(memberAdaptor);
    }

    private void validatePassword(MemberAdaptor memberAdaptor, AuthenticationToken authenticationToken) {
        if (memberAdaptor.checkPassword(authenticationToken.getPassword())) {
            memberAdaptor.clearPassword();
            return ;
        }
        throw new AuthenticationException();
    }

    public String extractJwtToken(Authentication authentication) {
        return jwtTokenProvider.createToken(((MemberAdaptor) authentication.getPrincipal()).getEmail());
    }

    public void pushSecurityContextInSession(HttpServletRequest request, SecurityContext securityContext) {
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }
}
