package atdd.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UserResponseDto {

    @JsonIgnore
    private Long id;
    private String email;
    @JsonProperty("이름")
    private String name;
    private String password;

    private UserResponseDto() { }

    public static UserResponseDto of(Long id, String email, String name, String password) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.id = id;
        responseDto.email = email;
        responseDto.name = name;
        responseDto.password = password;
        return responseDto;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponseDto)) return false;
        UserResponseDto that = (UserResponseDto) o;
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
        return "UserResponseDto{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
