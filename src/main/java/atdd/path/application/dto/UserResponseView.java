package atdd.path.application.dto;

import atdd.path.domain.Member;

public class UserResponseView {

	private String email;
	private String password;

	public UserResponseView() {
	}

	public UserResponseView(final String email, final String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public static UserResponseView of(final Member member) {
		return new UserResponseView(member.getEmail(), member.getPassword());
	}
}
