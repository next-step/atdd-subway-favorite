package atdd.user.dto;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class UserCreateRequestDto {

    @NotEmpty
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

    private UserCreateRequestDto() { }

    public static UserCreateRequestDto of(String email, String name, String password) {
        UserCreateRequestDto requestDto = new UserCreateRequestDto();
        requestDto.email = email;
        requestDto.name = name;
        requestDto.password = password;
        return requestDto;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCreateRequestDto)) return false;
        UserCreateRequestDto that = (UserCreateRequestDto) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(name, that.name) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }

    @Override
    public String toString() {
        return "UserCreateRequestDto{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
