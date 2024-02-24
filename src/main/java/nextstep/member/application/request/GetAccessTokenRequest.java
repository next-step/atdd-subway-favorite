package nextstep.member.application.request;

public class GetAccessTokenRequest {

    private String code;

    private GetAccessTokenRequest() {
    }

    private GetAccessTokenRequest(String code) {
        this.code = code;
    }

    public static GetAccessTokenRequest from(String code) {
        return new GetAccessTokenRequest(code);
    }

    public String getCode() {
        return code;
    }

}
