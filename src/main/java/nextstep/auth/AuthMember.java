package nextstep.auth;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthMember {

	private String email;
	private List<String> roles;
}
