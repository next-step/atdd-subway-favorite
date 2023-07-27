package nextstep.utils;

public enum GithubResponse {
    사용자("code", "token", "email@email.com", 20);

    private String code;
    private String accessToken;
    private String email;
    private int age;

    GithubResponse(String code, String accessToken, String email, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
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

    public int getAge() {
        return age;
    }
}
