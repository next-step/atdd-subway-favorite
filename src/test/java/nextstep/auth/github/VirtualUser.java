package nextstep.auth.github;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Getter
public enum VirtualUser {

    사용자1("user1@github.com", "password1", 10, "code1", "token1");

    VirtualUser(String email, String password, int age, String code, String token) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.code = code;
        this.token = token;
    }

    private final String email;
    private final String password;
    private final int age;
    private final String code;
    private final String token;

    public static Optional<VirtualUser> getUserByCode(final String code) {
        return Arrays.stream(values())
                .filter(u -> Objects.equals(u.getCode(), code))
                .findAny();
    }

    public static Optional<VirtualUser> getUserToken(final String token) {
        return Arrays.stream(values())
                .filter(u -> Objects.equals("Bearer " + u.getToken(), token))
                .findAny();
    }
}
