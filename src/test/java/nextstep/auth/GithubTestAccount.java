package nextstep.auth;

public enum GithubTestAccount {
    사용자1("code1", "accessToken1", "user1@nextstep.com", 10),
    사용자2("code2", "accessToken2", "user2@nextstep.com", 20),
    사용자3("code3", "accessToken3", "user3@nextstep.com", 30),
    사용자4("code4", "accessToken4", "user4@nextstep.com", 40);

    GithubTestAccount(String code, String accessToken, String email, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    private String code;
    private String accessToken;
    private String email;
    private int age;

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
