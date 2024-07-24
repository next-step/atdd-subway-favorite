package nextstep.subway.line.exception;

import nextstep.support.error.ApiException;
import nextstep.support.error.ErrorCode;

public class StationNotFoundInLineException extends ApiException {
  public StationNotFoundInLineException(Long id) {
    super(ErrorCode.STATION_NOT_FOUND_IN_LINE, "역 #" + id + "은 노선에 등록되어 있지 않습니다.");
  }
}
