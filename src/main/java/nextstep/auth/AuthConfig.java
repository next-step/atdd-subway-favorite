package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.SessionConverter;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.TokenConverter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.UserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
  private final UserDetailsService userDetailsService;
  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;

  public AuthConfig(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
    this.userDetailsService = userDetailsService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.objectMapper = objectMapper;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // Bean 주입으로 하려다가 일단 그냥 붙여넣었습니다.
    registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService, new SessionConverter())).addPathPatterns("/login/session");
    registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, new TokenConverter(objectMapper), jwtTokenProvider, objectMapper)).addPathPatterns("/login/token");
    registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
    registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
  }

  @Override
  public void addArgumentResolvers(List argumentResolvers) {
    argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
  }

}
