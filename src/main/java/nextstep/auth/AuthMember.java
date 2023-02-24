package nextstep.auth;

import nextstep.member.domain.Member;

import java.util.List;

public class AuthMember {
	private Long id;
	private String email;
	private Integer age;
	private List<String> roles;

	protected AuthMember() {
	}

	public AuthMember(Long id, String email, Integer age, List<String> roles) {
		this.id = id;
		this.email = email;
		this.age = age;
		this.roles = roles;
	}

	public static AuthMember of(Member member) {
		return new AuthMember(member.getId(), member.getEmail(), member.getAge(), member.getRoles());
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public Integer getAge() {
		return age;
	}

	public List<String> getRoles() {
		return roles;
	}
}
