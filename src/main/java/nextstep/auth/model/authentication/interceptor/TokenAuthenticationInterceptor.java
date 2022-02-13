package nextstep.auth.model.authentication.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.authentication.AuthenticationToken;
import nextstep.auth.model.authentication.UserDetails;
import nextstep.auth.model.authentication.service.UserDetailsService;
import nextstep.auth.model.context.Authentication;
import nextstep.auth.model.token.JwtTokenProvider;
import nextstep.auth.model.token.dto.TokenResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationInterceptor implements AuthenticationInterceptor {

    private UserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(UserDetailsService customUserDetailsService,
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
        String principle = authenticationToken.getEmail();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(principle);
        userDetails.validateCredential(authenticationToken.getPassword());

        return new Authentication(userDetails);
    }

    @Override
    public void afterAuthenticate(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenResponse tokenResponse = TokenResponse.from(createJwtToken(authentication));
        makeResponse(response, tokenResponse);
    }

    private String createJwtToken(Authentication authentication) {
        UserDetails userDetails = authentication.getPrincipal();
        return jwtTokenProvider.createToken(userDetails.getUsername());
    }

    private void makeResponse(HttpServletResponse response, TokenResponse tokenResponse) throws IOException {
        String responseToClient = objectMapper.writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
