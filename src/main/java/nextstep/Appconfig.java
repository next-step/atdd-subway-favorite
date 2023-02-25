package nextstep;

import nextstep.auth.config.AuthArgumentResolver;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.config.JwtTokenFilter;
import nextstep.member.application.config.AuthFavoriteArgumentResolver;
import nextstep.member.domain.MemberRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class Appconfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public Appconfig(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Bean
    public FilterRegistrationBean<JwtTokenFilter> bearerTokenFilter() {
        FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtTokenFilter());
        registrationBean.addUrlPatterns("/members/me","/favorites");

        return registrationBean;
    }
  
    @Bean
    public AuthArgumentResolver authArgumentResolver(){
        return new AuthArgumentResolver(jwtTokenProvider);
    }

    @Bean
    public AuthFavoriteArgumentResolver authFavoriteArgumentResolver(){
        return new AuthFavoriteArgumentResolver(jwtTokenProvider, memberRepository);
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver());
        resolvers.add(authFavoriteArgumentResolver());

        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
