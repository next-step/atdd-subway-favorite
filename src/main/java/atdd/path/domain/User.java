package atdd.path.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static atdd.path.dao.UserDao.*;

@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String name;
    private String password;
    private List<Favorite> favorites = new ArrayList<>();

    @Builder
    public User(Long id, String email, String name, String password, List<Favorite> favorites) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.favorites = favorites;
    }

    public User(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.favorites = new ArrayList<>();
    }

    public User(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.favorites = new ArrayList<>();
    }

    public static User of(Long id, String email, String name, String password) {
        return new User(null, email, name, password, new ArrayList<>());
    }


    public static User getUserByFindData(Map<String, Object> user) {
        return User.builder()
                .id((Long)user.get(ID_KEY))
                .email((String)user.get(EMAIL_KEY))
                .name((String) user.get(NAME_KEY))
                .build();
    }

    public static Map<String, Object> getSaveParameterByUser(User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ID_KEY, user.getId());
        parameters.put(NAME_KEY, user.getName());
        parameters.put(EMAIL_KEY, user.getEmail());
        parameters.put(PASSWORD_KEY, user.getPassword());
        return parameters;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
