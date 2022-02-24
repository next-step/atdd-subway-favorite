package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.domain.UserDetails;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenAuthenticationInterceptor implements HandlerInterceptor, AuthenticationConverter {

    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);

        String payload = new ObjectMapper().writeValueAsString(authentication.getPrincipal());
        String token = jwtTokenProvider.createToken(payload);
        TokenResponse tokenResponse = new TokenResponse(token);

        SecurityContext securityContext = new SecurityContext(authentication);
        SecurityContextHolder.setContext(securityContext);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return false;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String requestInfo = request.getReader()
                .lines()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        JSONObject jsonObject = new JSONObject(requestInfo);

        String principal = jsonObject.getString("email");
        String credentials = jsonObject.getString("password");

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        UserDetails userDetails = userDetailService.loadUserByUsername(
                authenticationToken.getPrincipal());

        userDetails.checkPassword(authenticationToken.getCredentials());

        return new Authentication(userDetails);
    }
}
