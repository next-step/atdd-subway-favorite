package nextstep.member.external;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum GithubFakeResponse {

    사용자1("aofijeowifjaoief", "access_token_1", "user1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "user2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "user3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "user4@email.com")
    ;

    private String code;
    private String accessToken;
    private String email;

    GithubFakeResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    private static final Map<String, String> accessTokenMap;
    private static final Map<String, String> emailMap;
    static  {
        accessTokenMap = Arrays.stream(GithubFakeResponse.values())
            .collect(Collectors.toMap(response -> response.code, response -> response.accessToken));

        emailMap = Arrays.stream(GithubFakeResponse.values())
            .collect(Collectors.toMap(response -> response.accessToken, response -> response.email));
    }


    public static String getAccessToken(String code) {
        return accessTokenMap.get(code);
    }

    public static String getEmail(String accessToken) {
        return emailMap.get(accessToken);
    }

}
