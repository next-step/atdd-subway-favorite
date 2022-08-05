package nextstep.member.domain;

import java.util.List;

import nextstep.auth.domain.AuthUser;

public class CustomAuthUser implements AuthUser {
	private String email;
	private String password;
	private List<String> authorities;

	public CustomAuthUser(String email, String password, List<String> authorities) {
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public static CustomAuthUser of(Member member) {
		return new CustomAuthUser(member.getEmail(), member.getPassword(), member.getRoles());
	}

	@Override
	public String getUserName() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public List<String> getAuthorities() {
		return this.authorities;
	}

	@Override
	public boolean isValidPassword(String password) {
		return this.password.equals(password);
	}
}
