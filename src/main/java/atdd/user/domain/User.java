package atdd.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private Long id;
    private String name;
    private String password;
    private String email;

    public User(){

    }

    public User(String name, String password, String email){
        this.name = name;
        this.password = password;
        this.email = email;
    }


    @Builder
    public User(Long id, String name, String password, String email){
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

//    public static User of(String name, String password){
//        return new User(name, password);
//    }
}
