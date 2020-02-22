package atdd.path.application.dto.User;

import atdd.path.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDetailResponseView {
    private Long id;
    private String name;
    private String email;

    @Builder
    public UserDetailResponseView(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserDetailResponseView toDtoEntity(User user) {
        return UserDetailResponseView.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
