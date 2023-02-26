package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRequest {
	private String email;
	private String password;
}
