package nextstep.auth.application.dto;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@Builder
@AllArgsConstructor
@ConfigurationProperties("github.client")
@ConstructorBinding
public class GithubClientProperties {
    private final String id;
    private final String secret;
}
