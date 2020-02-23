package atdd.user.application.dto;

import atdd.user.domain.User;
import lombok.Getter;

@Getter
public class UserResponseView {
    private Long id;
    private String name;
    private String password;
    private String email;

    public UserResponseView(){

    }

    public UserResponseView(Long id, String name, String password, String email){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;

    }

    public static User of(User user){
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }
}
