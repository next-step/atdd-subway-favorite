package nextstep.api.auth.domain.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GithubCodeResponse {

	private String code;
}
