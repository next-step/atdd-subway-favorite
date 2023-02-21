package nextstep.member.ui.dto;

public class LoginUser {

    private final String id;

    public LoginUser(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
            "id='" + id + '\'' +
            '}';
    }
}
