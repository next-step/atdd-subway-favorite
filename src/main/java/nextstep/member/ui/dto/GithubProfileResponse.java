package nextstep.member.ui.dto;

public class GithubProfileResponse {

    private String email;
    private int age;

    public GithubProfileResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }

    public GithubProfileResponse() {

    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
