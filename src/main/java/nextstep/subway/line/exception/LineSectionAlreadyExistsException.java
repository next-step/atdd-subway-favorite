package nextstep.subway.line.exception;

import nextstep.support.error.ApiException;
import nextstep.support.error.ErrorCode;

public class LineSectionAlreadyExistsException extends ApiException {
  public LineSectionAlreadyExistsException() {
    super(ErrorCode.BAD_REQUEST);
  }
}
