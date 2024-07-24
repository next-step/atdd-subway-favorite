package nextstep.subway.line.exception;

import nextstep.support.error.ApiException;
import nextstep.support.error.ErrorCode;

public class CannotRemoveLastLineSectionException extends ApiException {
  public CannotRemoveLastLineSectionException() {
    super(ErrorCode.CANNOT_REMOVE_LAST_LINE_SECTION);
  }
}
