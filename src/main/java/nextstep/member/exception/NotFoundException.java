package nextstep.member.exception;

public class NotFoundException extends RuntimeException {
	private static final String DEFAULT_MESSAGE = "요청한 데이터를 찾을 수 없습니다.";

	public NotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	public NotFoundException(ErrorMessage message) {
		super(message.getMessage());
	}
}
