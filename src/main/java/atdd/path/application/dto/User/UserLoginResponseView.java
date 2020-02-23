package atdd.path.application.dto.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponseView {
    private String accessToken;
    private String tokenType;


    @Builder
    public UserLoginResponseView(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public static UserLoginResponseView toDtoEntity(String accessToken, String tokenType) {
        return UserLoginResponseView.builder()
                .accessToken(accessToken)
                .tokenType(tokenType)
                .build();
    }
}
