package nextstep.oauth2;

import nextstep.auth.token.oauth2.github.GithubClientInterface;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;

import java.util.Objects;

public class TestGithubClient implements GithubClientInterface {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return GithubResponses.getToken(code);
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return new GithubProfileResponse(GithubResponses.getEmail(accessToken), 20);
    }

    private enum GithubResponses {
        user1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
        user2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
        user3("afnm93fmdodf", "access_token_3", "email3@email.com"),
        user4("fm04fndkaladmd", "access_token_4", "email4@email.com");

        private String code;
        private String accessToken;
        private String email;


        GithubResponses(String code, String accessToken, String email) {
            this.code = code;
            this.accessToken = accessToken;
            this.email = email;
        }

        public static String getToken(String code) {
            if (Objects.equals(user1.code, code)) {
                return user1.accessToken;
            }
            if (Objects.equals(user2.code, code)) {
                return user2.accessToken;
            }
            if (Objects.equals(user3.code, code)) {
                return user3.accessToken;
            }
            return user4.accessToken;
        }

        public static String getEmail(String accessToken) {
            if (Objects.equals(user1.accessToken, accessToken)) {
                return user1.email;
            }
            if (Objects.equals(user2.accessToken, accessToken)) {
                return user2.email;
            }
            if (Objects.equals(user3.accessToken, accessToken)) {
                return user3.email;
            }
            return user4.email;
        }
    }
}
