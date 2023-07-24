package nextstep.exception.notfoundexception;

public class MemberNotFoundException extends NotFoundException {
    private static final String ERROR_MESSAGE = "Member 가 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(ERROR_MESSAGE);
    }

}
