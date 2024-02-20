package nextstep.subway.auth.client.github.dto;

import java.util.Objects;

public class GithubProfileResponse {
    private String email;
    private Integer age;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(String email,
                                 Integer age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GithubProfileResponse that = (GithubProfileResponse) o;
        return Objects.equals(email, that.email) && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, age);
    }
}
