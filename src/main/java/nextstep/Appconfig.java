package nextstep;

import nextstep.auth.config.AuthArgumentResolver;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.config.JwtTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class Appconfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;

    public Appconfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
  
    @Bean
    public FilterRegistrationBean<JwtTokenFilter> bearerTokenFilter() {
        FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtTokenFilter());
        registrationBean.addUrlPatterns("/members/me");

        return registrationBean;
    }
  
    @Bean
    public AuthArgumentResolver authArgumentResolver(){
        return new AuthArgumentResolver(jwtTokenProvider);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
