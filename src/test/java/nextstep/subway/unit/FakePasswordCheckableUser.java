package nextstep.subway.unit;

import nextstep.member.domain.PasswordCheckableUser;

public class FakePasswordCheckableUser implements PasswordCheckableUser {
    private String email;
    private String password;

    private FakePasswordCheckableUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean checkPassword(String password) {
        return true;
    }

    public static FakePasswordCheckableUser create(String email, String password) {
        return new FakePasswordCheckableUser(email, password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
