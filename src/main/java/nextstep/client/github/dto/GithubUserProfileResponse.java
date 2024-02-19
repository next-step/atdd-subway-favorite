package nextstep.client.github.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GitHub에서 인증된 사용자의 응답을 나타냅니다.
 * 자세한 정보는 GitHub API 문서를 참조하세요:
 * https://docs.github.com/ko/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
 *
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GithubUserProfileResponse {

	private String email;
	private Long id;
	/**
	 * Github에서 username과 같은 의미 -> https://github.com/[username]
	 */
	private String login;
}
