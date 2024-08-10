package nextstep.utils.dtoMock;

import java.util.Arrays;

public enum GithubResponse {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 20, "password"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 30, "password"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 40, "password"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 50, "password");

    private String code;
    private String accessToken;
    private String email;
    private int age;
    private String password;

    GithubResponse(String code, String accessToken, String email, int age, String password) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    public static String getEmailByAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(response -> response.getAccessToken().equals(accessToken))
                .map(GithubResponse::getEmail)
                .findFirst()
                .orElse(null);
    }

    public static int getAgeByAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(response -> response.getAccessToken().equals(accessToken))
                .map(GithubResponse::getAge)
                .findFirst()
                .orElse(-1);
    }

    public static String getAccessTokenByCode(String code) {
        return Arrays.stream(values())
                .filter(response -> response.getCode().equals(code))
                .map(GithubResponse::getAccessToken)
                .findFirst()
                .orElse(null);
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

    public String getPassword() {
        return password;
    }
}

