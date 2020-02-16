package atdd.path.application.dto;

import lombok.Getter;

@Getter
public class LoginResponseView {
    private String accessToken;
    private String tokenType;
}
