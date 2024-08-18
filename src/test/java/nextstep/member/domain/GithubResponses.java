package nextstep.member.domain;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum GithubResponses {

    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
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

    private static Map<String, GithubResponses> cachedByCode = Stream.of(values()).collect(Collectors.toMap(GithubResponses::getCode, v -> v));

    public static Optional<GithubResponses> fromCode(String code) {
        return Optional.ofNullable(cachedByCode.get(code));
    }

    private static Map<String, GithubResponses> cachedByAccessCode = Stream.of(values()).collect(Collectors.toMap(GithubResponses::getAccessToken, v -> v));

    public static Optional<GithubResponses> fromAccessCode(String accessCode) {
        return Optional.ofNullable(cachedByAccessCode.get(accessCode));
    }
}
