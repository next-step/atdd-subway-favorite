package nextstep.fake;

public enum GithubResponse {
    사용자1("code1", "accessToken1","email1@email.com"),
    사용자2("code2", "accessToken2","email2@email.com"),
    사용자3("code3", "accessToken3","email3@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    GithubResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubResponse findByCode(String code) {
        for (GithubResponse githubResponse : GithubResponse.values()) {
            if (githubResponse.code.equals(code)) {
                return githubResponse;
            }
        }
        throw new IllegalArgumentException("해당하는 코드가 없습니다.");
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
}
