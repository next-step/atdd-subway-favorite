package atdd.path.application.dto;

import atdd.path.domain.User;
import lombok.Getter;

@Getter
public class UserResponseView {
    private Long id;
    private String name;
    private String password;

    public UserResponseView(){

    }

    public UserResponseView(Long id, String name, String password){
        this.id = id;
        this.name = name;

    }

    public static User of(User user){
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }
}
