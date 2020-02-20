package atdd.path.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

import static atdd.path.dao.UserDao.*;

@Getter
@Builder
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

    public static FindByEmailResponseView toDtoEntity(Map<String, Object> user) {
        return FindByEmailResponseView.builder()
                .id((Long)user.get(ID_KEY))
                .email(user.get(EMAIL_KEY).toString())
                .name(user.get(NAME_KEY).toString())
                .build();
    }
}
