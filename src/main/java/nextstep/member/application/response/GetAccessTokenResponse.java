package nextstep.member.application.response;

public class GetAccessTokenResponse {

    private String accessToken;

    private GetAccessTokenResponse(){}

    private GetAccessTokenResponse(String accessToken){
        this.accessToken = accessToken;
    }

    public static GetAccessTokenResponse from(String accessToken){
        return new GetAccessTokenResponse(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

}
