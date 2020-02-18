package atdd.path.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private Long id;
    private String name;
    private String password;

    public User(){

    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
    }

    @Builder
    public User(Long id, String name, String password){
        this.id = id;
        this.name = name;
        this.password = password;
    }

//    public static User of(String name, String password){
//        return new User(name, password);
//    }
}
