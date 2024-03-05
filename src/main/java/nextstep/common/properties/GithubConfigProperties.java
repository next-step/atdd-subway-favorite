package nextstep.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Setter
@Getter
@ConfigurationProperties(prefix = "github")
@ConfigurationPropertiesScan
public class GithubConfigProperties {

    private String clientId;
    private String clientSecret;
    private String accessTokenUrl;
    private String profileUrl;

}
