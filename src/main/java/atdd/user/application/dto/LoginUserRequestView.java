package atdd.user.application.dto;

import lombok.Getter;

@Getter
public class LoginUserRequestView {
    String password;
    String email;

    public LoginUserRequestView(){

    }

    public LoginUserRequestView(String password, String email){
        this.password = password;
        this.email = email;
    }

}
