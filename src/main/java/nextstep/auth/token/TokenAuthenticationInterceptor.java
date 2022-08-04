package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.NoMoreProceedAuthenticationFilter;
import nextstep.auth.authentication.ValidateUser;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends NoMoreProceedAuthenticationFilter {

    private UserDetailsService userDetailsService;

    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        User user = userDetailsService.loadUserByUsername(principal);

        ValidateUser validate = new ValidateUser();
        validate.execute(credentials, user);

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);

        return proceed();
    }
}
