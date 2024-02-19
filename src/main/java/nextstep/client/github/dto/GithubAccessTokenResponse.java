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
public class GithubAccessTokenResponse {
	private String accessToken;
	private String scope;
	private String tokenType;

}
