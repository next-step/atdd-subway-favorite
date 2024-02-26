package nextstep.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("github.client")
public class GithubClientProperties {

  private String baseUrl;
  private String id;
  private String secret;
}
