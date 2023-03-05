package nextstep.fixture;

public enum GitHubProfileFixture {

    ALEX_GITHUB("alex-code", "alex-token"),
    JADE_GITHUB("jade-code", "jade-token"),
    ADMIN_GITHUB("admin-code", "admin-token"),
    ;

    private final String code;
    private final String token;

    GitHubProfileFixture(final String code, final String token) {
        this.code = code;
        this.token = token;
    }

    public String 권한_증서_코드() {
        return code;
    }

    public String 인증_토큰() {
        return token;
    }
}
