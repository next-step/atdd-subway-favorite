package nextstep.utils;

import java.util.Arrays;
import java.util.function.Predicate;
import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.exception.UnAuthorizedException;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Primary
@Profile("test")
@Component
public class FakeGithubClientImpl implements GithubClient {

    public enum GithubResponses {
        사용자1("832ovnq039hfjn", "access_token_1", "admin@email.com"),
        사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
        사용자3("a3hnfnoew92", "access_token_3", "email3@email.com"),
        사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

        private String code;
        private String accessToken;
        private String email;

        GithubResponses(final String code, final String accessToken, final String email) {
            this.code = code;
            this.accessToken = accessToken;
            this.email = email;
        }

        public static GithubResponses findBy(final Predicate<GithubResponses> predicate) {
            return Arrays.stream(GithubResponses.values())
                    .filter(predicate)
                    .findFirst()
                    .orElseThrow(UnAuthorizedException::new);
        }
    }

    public String getAccessTokenFromGithub(String code) {
        return GithubResponses.findBy(githubResponse -> githubResponse.code.equals(code)).accessToken;
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return new GithubProfileResponse(
                GithubResponses.findBy(githubResponse -> githubResponse.accessToken.equals(accessToken)).email
        );
    }
}
