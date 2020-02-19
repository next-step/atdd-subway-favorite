package atdd.path.application.dto;

import atdd.path.domain.User;
import lombok.Builder;

import java.util.List;
import java.util.Map;

import static atdd.path.dao.UserDao.*;

@Builder
public class FindByEmailResponseView {
    private List<User> users;

    @Builder
    public FindByEmailResponseView(List<Map<String, Object>> mapUsers) {
        mapUsers.stream()
                .map(mapUser -> this.users.stream()
                        .map(user -> User.builder()
                                .email(mapUser.get(EMAIL_KEY).toString())
                                .name(mapUser.get(NAME_KEY).toString())
                                .id(Long(mapUser.get(ID_KEY))))
        this.users = mapUsers;
    }

    public FindByEmailResponseView toDtoEntity(List<Map<String, Object>> user) {
        return FindByEmailResponseView.builder()
                .users(user)
                .build();
    }

}
