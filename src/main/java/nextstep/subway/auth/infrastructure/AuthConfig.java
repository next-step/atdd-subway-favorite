package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.auth.ui.LoginMemberPort;
import nextstep.subway.auth.ui.TokenAuthenticate;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.session.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.token.TokenSecurityContextPersistenceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private TokenAuthenticate tokenAuthenticate;
    private JwtTokenProvider jwtTokenProvider;
    private LoginMemberPort loginMemberPort;

    public AuthConfig(TokenAuthenticate tokenAuthenticate, JwtTokenProvider jwtTokenProvider, LoginMemberPort loginMemberPort) {
        this.tokenAuthenticate = tokenAuthenticate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginMemberPort = loginMemberPort;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(tokenAuthenticate)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(tokenAuthenticate, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider, loginMemberPort));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
