package nextstep.exception.member;

import nextstep.exception.ErrorMessage;
import nextstep.exception.SubwayException;

public class ResourceNotFoundException extends SubwayException {
    public ResourceNotFoundException() {
        super(ErrorMessage.RESOURCE_NOT_FOUND);
    }
}
