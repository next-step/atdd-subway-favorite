package atdd.configure;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import atdd.auth.interceptor.AuthorizationArgumentResolver;
import atdd.auth.interceptor.AuthorizationInterceptor;

@Configuration
public class AuthConfig implements WebMvcConfigurer{
  private AuthorizationInterceptor authorizationInterceptor;
  private AuthorizationArgumentResolver authorizationArgumentResolver;

  public AuthConfig(AuthorizationInterceptor authorizationInterceptor,
      AuthorizationArgumentResolver authorizationArgumentResolver) {
    this.authorizationInterceptor = authorizationInterceptor;
    this.authorizationArgumentResolver = authorizationArgumentResolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(authorizationArgumentResolver);
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
      .addInterceptor(authorizationInterceptor)
      .addPathPatterns("/user/me");
    WebMvcConfigurer.super.addInterceptors(registry);
  }
  
}
