package atdd.path.exception;

public class MemberNotFoundException extends RuntimeException {

	public MemberNotFoundException() {
		super();
	}

	public MemberNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MemberNotFoundException(final String message) {
		super(message);
	}

	public MemberNotFoundException(final Throwable cause) {
		super(cause);
	}
}
