package atdd.path.application.dto;

public class CreateAccessTokenRequestView {

	private String email;
	private String password;

	public CreateAccessTokenRequestView() {
	}

	public CreateAccessTokenRequestView(final String email, final String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public static CreateAccessTokenRequestView of(final String email, final String password) {
		return new CreateAccessTokenRequestView(email, password);
	}
}
