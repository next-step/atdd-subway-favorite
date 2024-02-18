package nextstep.api.auth.domain.dto.outport;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@Value
@AllArgsConstructor(staticName = "of")
public class OAuthUserInfo {

	String email;
}
