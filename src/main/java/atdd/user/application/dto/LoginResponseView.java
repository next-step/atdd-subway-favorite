package atdd.user.application.dto;

import lombok.Getter;

@Getter
public class LoginResponseView {
    private String accessToken;
    private String tokenType;

    public LoginResponseView(){

    }

    public LoginResponseView(String accessToken, String tokenType){
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

}
