package nextstep.common;

public class NotFoundMemberException extends NotFoundException {

    public NotFoundMemberException(String memberEmail) {
        super(String.format("not found member : %s", memberEmail));
    }
}
