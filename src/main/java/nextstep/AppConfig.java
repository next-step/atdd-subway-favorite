package nextstep;

import nextstep.member.ui.filter.BearerTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public FilterRegistrationBean<BearerTokenFilter> bearerTokenFilter() {
        FilterRegistrationBean<BearerTokenFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new BearerTokenFilter());
        registrationBean.addUrlPatterns("/members/*");

        return registrationBean;
    }
}
