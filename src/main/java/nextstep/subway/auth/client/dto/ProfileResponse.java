package nextstep.subway.auth.client.dto;

public class ProfileResponse {
    private String email;
    private Integer age;

    public ProfileResponse(String email,
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
}
