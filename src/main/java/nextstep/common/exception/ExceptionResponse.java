package nextstep.common.exception;

public class ExceptionResponse {

    int httpStatusCode;

    public ExceptionResponse() {}
    public ExceptionResponse(int httpStatusCode) { this.httpStatusCode = httpStatusCode; }

    public int getHttpStatusCode() { return httpStatusCode; }
}
