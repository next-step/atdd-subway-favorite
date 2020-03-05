package atdd.path.application.dto.user;

import atdd.path.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static atdd.path.dao.UserDao.*;

@Getter
@Builder
@NoArgsConstructor
public class FindByEmailResponseView {
    private Long id;
    private String email;
    private String name;

    @Builder
    public FindByEmailResponseView(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }
    public static FindByEmailResponseView toDtoEntity(List<Map<String, Object>> users) {
        if (users.isEmpty()) {
            return new FindByEmailResponseView();
        }

        Map<String, Object> user = users.get(FIRST_INDEX);
        return FindByEmailResponseView.builder()
                .id((Long)user.get(ID_KEY))
                .email(user.get(EMAIL_KEY).toString())
                .name(user.get(NAME_KEY).toString())
                .build();
    }
}
