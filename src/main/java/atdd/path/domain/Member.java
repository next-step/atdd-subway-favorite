package atdd.path.domain;

public class Member {

	private Long id;
	private String email;
	private String name;
	private String password;

	public Member(final Long id, final String email, final String name, final String password) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
	}

	public Member(final String email, final String name, final String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}

	public Long getId() {
		return id;
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
}
