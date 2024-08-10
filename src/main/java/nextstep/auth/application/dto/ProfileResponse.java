package nextstep.auth.application.dto;

public class ProfileResponse {
    private String email;
    private int age;

    public ProfileResponse() {
    }

    public ProfileResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }

    public static ProfileResponse of(String email, int age) {
        return new ProfileResponse(email, age);
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}

