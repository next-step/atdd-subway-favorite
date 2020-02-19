package atdd.member.application.dto;

import java.io.Serializable;

public class JwtTokenResponseView {

    private String token;

    public JwtTokenResponseView() {
    }

    public JwtTokenResponseView(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
