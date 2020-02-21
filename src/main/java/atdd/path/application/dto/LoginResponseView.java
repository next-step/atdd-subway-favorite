package atdd.path.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseView {
    private String accessToken;
    private String tokenType;

    @Builder
    public LoginResponseView(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;

    }
}
