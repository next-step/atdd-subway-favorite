package atdd.path.application.dto;

import atdd.path.domain.Member;

public class CreateMemberRequestView {

	private String email;
	private String name;
	private String password;

	public CreateMemberRequestView() {
	}

	public CreateMemberRequestView(final String email, final String name, final String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public Member toMember() {
		return new Member(email, name, password);
	}
}
