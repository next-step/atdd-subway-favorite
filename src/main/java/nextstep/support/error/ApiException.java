package nextstep.support.error;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
  private final ErrorCode errorCode;
  private final Object data;

  public ApiException(ErrorCode errorCode, Object data) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.data = data;
  }

  public ApiException(ErrorCode errorCode) {
    this(errorCode, null);
  }
}
