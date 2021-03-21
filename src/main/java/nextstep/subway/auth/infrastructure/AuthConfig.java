package nextstep.subway.auth.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import nextstep.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.session.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.token.TokenAuthenticationConverter;
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
  private ObjectMapper objectMapper;

  public AuthConfig(CustomUserDetailsService userDetailsService,
      JwtTokenProvider jwtTokenProvider,ObjectMapper objectMapper) {
    this.userDetailsService = userDetailsService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.objectMapper = objectMapper;
  }

  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService, new SessionAuthenticationConverter()))
        .addPathPatterns("/login/session");
    registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService,new TokenAuthenticationConverter(objectMapper),jwtTokenProvider,objectMapper))
        .addPathPatterns("/login/token");
    registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
    registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
  }

  @Override
  public void addArgumentResolvers(List argumentResolvers) {
    argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
  }
}
