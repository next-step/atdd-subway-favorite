package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.application.handler.IssueTokenSuccessHandler;
import nextstep.subway.auth.application.handler.SimpleUrlAuthenticationFailureHandler;
import nextstep.subway.auth.application.provider.AuthenticationManager;
import nextstep.subway.auth.application.provider.AuthenticationProvider;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    private AuthenticationManager authenticationManager;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(new IssueTokenSuccessHandler(jwtTokenProvider), new SimpleUrlAuthenticationFailureHandler());
        this.authenticationManager = new AuthenticationProvider(userDetailsService);
    }

    @Override
    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        return authenticationManager.authenticate(authenticationToken);
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }
}
