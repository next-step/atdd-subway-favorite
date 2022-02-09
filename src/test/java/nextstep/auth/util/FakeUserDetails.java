package nextstep.auth.util;

import nextstep.auth.UserDetails;

public class FakeUserDetails implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer age;

    public FakeUserDetails(final Long id, final String email, final String password, final Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }
}
