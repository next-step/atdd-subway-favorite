package nextstep.support.error;

import lombok.Getter;

@Getter
public class ErrorResponse {
  private final String message;
  private final Object data;

  private ErrorResponse(String message, Object data) {
    this.message = message;
    this.data = data;
  }

  public ErrorResponse(ErrorCode errorCode) {
    this(errorCode.getMessage(), null);
  }

  public ErrorResponse(ErrorCode errorCode, Object data) {
    this(errorCode.getMessage(), data);
  }

  public static ErrorResponse from(ErrorCode errorCode) {
    return new ErrorResponse(errorCode);
  }

  public static ErrorResponse of(ErrorCode errorCode, Object data) {
    return new ErrorResponse(errorCode, data);
  }
}
