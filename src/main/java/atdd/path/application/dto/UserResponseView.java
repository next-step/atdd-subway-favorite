package atdd.path.application.dto;

import atdd.path.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseView {
    private Long id;
    private String email;
    private String name;

    @Builder
    public UserResponseView(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserResponseView of(User user) {
        return UserResponseView.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
