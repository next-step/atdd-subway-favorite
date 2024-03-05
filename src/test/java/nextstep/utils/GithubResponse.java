package nextstep.utils;

public enum GithubResponse {
    회원("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    토큰_없음("fau3nfin93dmn", null, null, 0),
    사용자_정보_없음("afnm93fmdodf", "access_token_3", null, 0),
    비회원("fm04fndkaladmd", "access_token_4", "email4@email.com", 35);

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
