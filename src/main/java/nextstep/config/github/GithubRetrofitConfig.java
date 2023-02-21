package nextstep.config.github;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import nextstep.infra.github.GithubClient;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
    GithubOAuthProperty.class,
    GithubOAuthProperty.Client.class,
    GithubOAuthProperty.Url.class,
    GithubOAuthProperty.Timeout.class
})
public class GithubRetrofitConfig {

    private final GithubOAuthProperty githubOAuthProperty;

    @Bean
    OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.connectTimeout(githubOAuthProperty.getConnectionTimeout(), TimeUnit.SECONDS)
            .writeTimeout(githubOAuthProperty.getWriteTimeout(), TimeUnit.SECONDS)
            .readTimeout(githubOAuthProperty.getReadTimeout(), TimeUnit.SECONDS)
            .build();
    }

    @Bean
    GithubClient githubClient(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder.client(okHttpClient)
            .baseUrl(githubOAuthProperty.getBaseUrl())
            .build();

        return retrofit.create(GithubClient.class);
    }
}
