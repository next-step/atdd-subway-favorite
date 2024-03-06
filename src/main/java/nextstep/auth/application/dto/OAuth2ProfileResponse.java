package nextstep.auth.application.dto;

public class OAuth2ProfileResponse {
    private String email;
    private int age;

    public OAuth2ProfileResponse() {}
    public OAuth2ProfileResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
