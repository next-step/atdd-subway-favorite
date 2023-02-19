package nextstep.member.config;

import lombok.RequiredArgsConstructor;
import nextstep.member.ui.GithubClient;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({
        GithubOauthProperty.class,
        GithubOauthProperty.Client.class,
        GithubOauthProperty.Url.class
})
@RequiredArgsConstructor
public class GithubRetrofitConfig {

    private final GithubOauthProperty githubOauthProperty;

    @Bean
    OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    GithubClient githubClient(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder.client(okHttpClient)
                .baseUrl(githubOauthProperty.getBaseUrl())
                .build();

        return retrofit.create(GithubClient.class);
    }
}
