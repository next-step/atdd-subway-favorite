package nextstep.auth.interceptor.nonchain;

import lombok.Getter;

@Getter
public class MemberInfo {
	private String email;
	private String password;

	public MemberInfo(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
