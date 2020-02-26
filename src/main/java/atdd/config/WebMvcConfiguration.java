package atdd.config;

import atdd.security.LoginUserInfoMethodArgumentResolver;
import atdd.security.LoginUserRegistry;
import atdd.security.UserAuthorizationFilter;
import atdd.user.dto.JsonWebTokenInfo;
import atdd.user.service.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserInfoMethodArgumentResolver(null));
    }

    @Bean
    public LoginUserInfoMethodArgumentResolver loginUserInfoMethodArgumentResolver(LoginUserRegistry loginUserRegistry) {
        return new LoginUserInfoMethodArgumentResolver(loginUserRegistry);
    }

    @Bean
    @ConfigurationProperties("security.jwt.token")
    public JsonWebTokenInfo jsonWebTokenInfo() {
        return new JsonWebTokenInfo();
    }

    @Bean
    public FilterRegistrationBean<UserAuthorizationFilter> userAuthorizationFilterFilterRegistrationBean(UserAuthorizationFilter userAuthorizationFilter) {
        FilterRegistrationBean<UserAuthorizationFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(userAuthorizationFilter);
        filterRegistrationBean.setName("userAuthorizationFilter");
        filterRegistrationBean.addUrlPatterns("/users/me");
        return filterRegistrationBean;
    }

    @Bean
    public UserAuthorizationFilter userAuthorizationFilter(AuthorizationService authorizationService,
                                                           ObjectMapper objectMapper) {

        return new UserAuthorizationFilter(authorizationService, objectMapper);
    }

}
