package nextstep.auth.domain.infrastructure.github;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GithubConfig {
    private final String clientId;
    private final String clientSecret;

    public GithubConfig(
            @Value("${github.client-id}") String clientId,
            @Value("${github.client-secret}") String clientSecret
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
