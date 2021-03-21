package nextstep.subway.auth.infrastructure;

import java.util.List;
import nextstep.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.session.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.token.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

  private CustomUserDetailsService userDetailsService;
  private JwtTokenProvider jwtTokenProvider;

  public AuthConfig(CustomUserDetailsService userDetailsService,
      JwtTokenProvider jwtTokenProvider) {
    this.userDetailsService = userDetailsService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService,
        new SessionAuthenticationConverter())).addPathPatterns("/login/session");
    registry
        .addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider))
        .addPathPatterns("/login/token");
    registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
    registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
  }

  @Override
  public void addArgumentResolvers(List argumentResolvers) {
    argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
  }
}
