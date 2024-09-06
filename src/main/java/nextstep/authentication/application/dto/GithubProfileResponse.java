package nextstep.authentication.application.dto;

import java.util.Objects;

public class GithubProfileResponse {

    private String email;
    private int age;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GithubProfileResponse that = (GithubProfileResponse) o;
        return age == that.age && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, age);
    }
}
