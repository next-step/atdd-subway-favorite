package nextstep.fake;

import java.util.function.Predicate;
import java.util.stream.Stream;
import nextstep.member.application.dto.github.GithubProfileResponse;

public enum GithubResponses {
        관리자("abcdefghijklmnop", "access_token", "admin@email.com"),
        사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
        사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
        사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
        사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

        private final String code;
        private final String accessToken;
        private final String email;

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

        public static GithubProfileResponse find(Predicate<GithubResponses> predicate) {
            return Stream.of(values())
                    .filter(predicate)
                    .map(response -> new GithubProfileResponse(response.code, response.accessToken, response.email))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
