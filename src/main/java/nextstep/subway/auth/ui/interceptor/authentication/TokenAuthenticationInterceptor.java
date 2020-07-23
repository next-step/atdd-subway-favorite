package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.application.converter.AuthenticationConverter;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.util.ConvertUtils;
import nextstep.subway.util.HttpResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(AuthenticationConverter converter, UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(converter, userDetailsService);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Objects.requireNonNull(authentication, "Authentication is null.");

        final String payload = ConvertUtils.stringify(authentication.getPrincipal());
        final String jwtToken = jwtTokenProvider.createToken(payload);

        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        HttpResponseUtils.write(response, () -> ConvertUtils.stringify(new TokenResponse(jwtToken)));
    }
}
