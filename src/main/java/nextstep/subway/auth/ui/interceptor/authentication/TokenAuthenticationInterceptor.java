package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.converter.AuthenticationConverter;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.util.ConvertUtils;
import nextstep.subway.util.HttpResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationConverter converter;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(AuthenticationConverter converter, CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.converter = converter;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final AuthenticationToken authToken = converter.convert(request);
        final Authentication authentication = authenticate(authToken);
        Objects.requireNonNull(authentication, "Authentication is null.");

        final String payload = ConvertUtils.stringify(toMemberResponse((LoginMember) authentication.getPrincipal()));
        final String jwtToken = jwtTokenProvider.createToken(payload);

        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        HttpResponseUtils.write(response, () -> ConvertUtils.stringify(new TokenResponse(jwtToken)));
        return false;
    }

    private Authentication authenticate(AuthenticationToken token) {
        final String principal = token.getPrincipal();
        final LoginMember loginMember = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(loginMember, token);

        return new Authentication(loginMember);
    }

    private void checkAuthentication(LoginMember loginMember, AuthenticationToken token) {
        if (loginMember == null) {
            throw new RuntimeException("No such user in repository.");
        }

        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new RuntimeException("Wrong password.");
        }
    }

    private MemberResponse toMemberResponse(LoginMember loginMember) {
        return new MemberResponse(loginMember.getId(), loginMember.getEmail(), loginMember.getAge());
    }
}
