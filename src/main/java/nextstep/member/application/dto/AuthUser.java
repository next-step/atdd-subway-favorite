package nextstep.member.application.dto;

import java.util.List;

public class AuthUser {
	private String email;
	private List<String> roleType;

	private AuthUser(String email, List<String> roleType) {
		this.email = email;
		this.roleType = roleType;
	}

	public static AuthUser of(String email, List<String> roleType) {
		return new AuthUser(email, roleType);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoleType() {
		return roleType;
	}

	public void setRoleType(List<String> roleType) {
		this.roleType = roleType;
	}
}
