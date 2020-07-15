package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.application.handler.IssueTokenSuccessHandler;
import nextstep.subway.auth.application.handler.SimpleUrlAuthenticationFailureHandler;
import nextstep.subway.auth.application.provider.AuthenticationManager;
import nextstep.subway.auth.application.provider.AuthenticationProvider;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    private static final String REGEX = ":";
    private AuthenticationManager authenticationManager;

    public TokenAuthenticationInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(new IssueTokenSuccessHandler(jwtTokenProvider), new SimpleUrlAuthenticationFailureHandler());
        this.authenticationManager = new AuthenticationProvider(userDetailsService);
    }

    @Override
    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationToken authenticationToken = convert(request);
        return authenticationManager.authenticate(authenticationToken);
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);

        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decodedString = new String(decodedBytes);

        String principal = decodedString.split(REGEX)[0];
        String credentials = decodedString.split(REGEX)[1];

        return new AuthenticationToken(principal, credentials);
    }
}
