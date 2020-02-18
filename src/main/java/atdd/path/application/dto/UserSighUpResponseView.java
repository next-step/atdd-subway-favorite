package atdd.path.application.dto;

import atdd.path.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSighUpResponseView {
    private Long id;
    private String email;
    private String name;

    @Builder
    public UserSighUpResponseView(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserSighUpResponseView toDtoEntity(User user) {
        return UserSighUpResponseView.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}

