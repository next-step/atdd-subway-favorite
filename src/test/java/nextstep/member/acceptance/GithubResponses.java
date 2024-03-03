package nextstep.member.acceptance;

public enum GithubResponses {
    사용자1("code1", "access_token1", "email1@email.com", "password1", 10),
    사용자2("code2", "access_token2", "email2@email.com", "password2", 20);

    private String code;
    private String accessToken;
    private String email;
    private String password;
    private int age;

    GithubResponses(String code, String accessToken, String email, String password, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }
}
