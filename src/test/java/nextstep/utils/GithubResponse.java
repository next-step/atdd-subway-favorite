package nextstep.utils;

public enum GithubResponse {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 30),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 40),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 50);

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

    public static String getEmailByAccessToken(String accessToken) {
        for (GithubResponse response : values()) {
            if (response.getAccessToken().equals(accessToken)) {
                return response.getEmail();
            }
        }
        return null;
    }

    public static int getAgeByAccessToken(String accessToken) {
        for (GithubResponse response : values()) {
            if (response.getAccessToken().equals(accessToken)) {
                return response.getAge();
            }
        }
        return -1;
    }

    public static String getAccessTokenByCode(String code) {
        for (GithubResponse response : values()) {
            if (response.getCode().equals(code)) {
                return response.getAccessToken();
            }
        }
        return null;
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

