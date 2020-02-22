package atdd.path.application.dto.User;

import atdd.path.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequestView {
    private String email;
    private String password;


    @Builder
    public UserLoginRequestView(String accessToken, String tokenType) {
        this.email = accessToken;
        this.password = tokenType;
    }

    public static User toEntity(UserSighUpRequestView user) {
        return User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
