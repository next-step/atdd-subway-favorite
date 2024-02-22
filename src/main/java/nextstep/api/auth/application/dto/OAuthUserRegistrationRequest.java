package nextstep.api.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/02/20
 */
@Data
@AllArgsConstructor(staticName = "of")
public class OAuthUserRegistrationRequest {
	private String email;
}
