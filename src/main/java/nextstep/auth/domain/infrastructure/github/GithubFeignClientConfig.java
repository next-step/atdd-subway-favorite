package nextstep.auth.domain.infrastructure.github;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class GithubFeignClientConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(HttpHeaders.ACCEPT, "application/vnd.github+json");
            requestTemplate.header(HttpHeaders.CONTENT_TYPE, "application/json");
            requestTemplate.header("X-GitHub-Api-Version", "2022-11-28");
        };
    }
}
