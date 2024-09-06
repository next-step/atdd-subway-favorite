package nextstep.utils;

import java.util.Arrays;

public enum UserInformation {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", "1234", 20, 1L),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", "5678", 19, 2L),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", "1q2w", 40, 3L),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", "qwer", 14, 4L);

    private String code;
    private String accessToken;
    private String email;
    private String password;
    private int age;
    private Long id;

    UserInformation(String code, String accessToken, String email, String password, int age, Long id) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.password = password;
        this.age = age;
        this.id = id;
    }

    public static String lookUpAccessToken(String code) {
        return Arrays.stream(values())
                .filter(response -> response.code.equals(code))
                .findFirst()
                .map(UserInformation::getAccessToken)
                .orElseThrow(IllegalArgumentException::new);
    }

    public static UserInformation lookUp(String accessToken) {
        return Arrays.stream(values())
                .filter(response -> response.accessToken.equals(accessToken))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
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

    public Long getId() {
        return id;
    }
}