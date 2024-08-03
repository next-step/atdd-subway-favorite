package nextstep.member.application.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", true),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", true),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", true),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", true),
    CODE_없는_사용자(null, "access_token_5", "email5@email.com"),
    ACCESS_TOKEN_없는_사용자("code6", null, "email6@email.com"),
    PROFILE_없는_사용자("code7", "access_token_7", "email7@email.com"),
    ;

    public static final String DEFAULT_PASSWORD = "default_password";

    public static final String WRONG_PASSWORD = "wrong_password";

    public static final int DEFAULT_AGE = 30;

    private static final Map<String, GithubResponses> codeMap = buildCodeMap();

    private static final Map<String, GithubResponses> accessTokenMap = buildAccessTokenMap();

    private static Map<String, GithubResponses> buildCodeMap() {
        return Arrays.stream(values())
            .collect(Collectors.toMap(GithubResponses::getCode, Function.identity()));
    }

    private static Map<String, GithubResponses> buildAccessTokenMap() {
        return Arrays.stream(values())
            .collect(Collectors.toMap(GithubResponses::getAccessToken, Function.identity()));
    }

    private String code;
    private String accessToken;
    private String email;

    private boolean registered;

    GithubResponses(String code, String accessToken, String email) {
        this(code, accessToken, email, false);
    }

    GithubResponses(String code, String accessToken, String email, boolean registered) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.registered = registered;
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

    public boolean isRegistered() {
        return registered;
    }

    public static GithubResponses findByCode(String code) {
        if (isInvalidCode(code)) {
            throw new IllegalArgumentException(String.format("Invalid code: %s", code));
        }

        return codeMap.get(code);
    }

    private static boolean isInvalidCode(String code) {
        return !StringUtils.hasText(code) || !codeMap.containsKey(code);
    }

    public static GithubResponses findByAccessToken(String accessToken) {
        if (isInvalidAccessToken(accessToken)) {
            throw new IllegalArgumentException(String.format("Invalid access token: %s", accessToken));
        }

        return accessTokenMap.get(accessToken);
    }

    private static boolean isInvalidAccessToken(String accessToken) {
        return !StringUtils.hasText(accessToken) || !accessTokenMap.containsKey(accessToken);
    }
}
