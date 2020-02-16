package atdd.path.application.dto;

import static atdd.path.util.MaskingUtils.getMasked;

public class CreateMemberResponseView {

	private Long id;
	private String email;
	private String name;
	private String password;

	public CreateMemberResponseView(final Long id, final String email, final String name, final String password) {
		this.id = id;
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
		return getMasked(password);
	}
}
