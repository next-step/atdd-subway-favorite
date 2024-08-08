package nextstep.member.application.dto;

public class GithubProfileResponse {
    private String email;
    private int age;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }

    public static GithubProfileResponse of(String email, int age) {
        return new GithubProfileResponse(email, age);
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}

