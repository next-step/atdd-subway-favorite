package nextstep.client.github.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/02/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GithubAccessTokenRequest {
	private String code;
	private String clientId;
	private String clientSecret;
}
