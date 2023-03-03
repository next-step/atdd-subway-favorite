package nextstep.member.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;

import nextstep.member.exception.ErrorMessage;
import nextstep.member.exception.NotFoundException;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;

	private String password;

	private Integer age;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "MEMBER_ROLE",
		joinColumns = @JoinColumn(name = "id", referencedColumnName = "id")
	)

	@Column(name = "role")
	private List<String> roles;

	public Member() {
	}

	public Member(String email, String password, Integer age) {
		this.email = email;
		this.password = password;
		this.age = age;
		this.roles = List.of(RoleType.ROLE_MEMBER.name());
	}

	public Member(String email, String password, Integer age, List<String> roles) {
		this.email = email;
		this.password = password;
		this.age = age;
		this.roles = roles;
	}

	public static Member ofCreatedByGithub(String email) {
		return new Member(email, null, null);
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Integer getAge() {
		return age;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void update(Member member) {
		this.email = member.email;
		this.password = member.password;
		this.age = member.age;
	}

	public boolean checkPassword(String password) {
		return Objects.equals(this.password, password);
	}

	public void validPassword(String password) {
		if (!this.checkPassword(password)) {
			throw new NotFoundException(ErrorMessage.NOT_FOUND_MEMBER_BY_PASSWORD);
		}
	}
}
