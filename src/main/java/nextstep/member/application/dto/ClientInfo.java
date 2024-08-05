package nextstep.member.application.dto;

import lombok.Getter;

@Getter
public class ClientInfo {

    private String url;
    private String clientId;
    private String clientSecret;

    public ClientInfo(String url, String clientId, String clientSecret) {
        this.url = url;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
