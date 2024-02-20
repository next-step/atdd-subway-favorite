package nextstep.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@Builder
@AllArgsConstructor
@ConfigurationProperties("github.url")
@ConstructorBinding
public class GithubUrlProperties {
    private final String accessToken;
    private final String email;
}
