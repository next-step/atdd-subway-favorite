package nextstep.auth.util;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Getter
public enum VirtualUser {

    사용자1("user1@github.com", "password1", 10, "code1", "token1", false),
    만료된사용자("user2@github.com", "password2", 11, "code2", "token2", true);

    VirtualUser(String email, String password, int age, String code, String token, boolean expired) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.code = code;
        this.token = token;
        this.expired = expired;
    }

    private final String email;
    private final String password;
    private final int age;
    private final String code;
    private final String token;
    private final boolean expired;

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

    public boolean isValid() {
        return !this.expired;
    }
}
