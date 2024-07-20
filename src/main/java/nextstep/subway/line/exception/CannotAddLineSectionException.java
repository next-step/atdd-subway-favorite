package nextstep.subway.line.exception;

import nextstep.support.error.ApiException;
import nextstep.support.error.ErrorCode;

public class CannotAddLineSectionException extends ApiException {
  public CannotAddLineSectionException() {
    super(ErrorCode.CANNOT_ADD_LINE_SECTION);
  }
}
