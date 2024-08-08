package nextstep.member.application;

import nextstep.member.domain.GithubOAuth2Client;
import nextstep.member.payload.AccessTokenRequest;
import nextstep.member.payload.AccessTokenResponse;
import nextstep.member.payload.GithubUserInfoResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Primary
public class FakeGithubOAuth2Client implements GithubOAuth2Client {

    @Override
    public AccessTokenResponse getAccessToken(final AccessTokenRequest request) {
        GithubResponses githubResponses = GithubResponses.findByCode(request.getCode());
        return new AccessTokenResponse(githubResponses.getAccessToken());
    }

    @Override
    public GithubUserInfoResponse getUserInfo(final String accessToken) {
        GithubResponses githubResponses = GithubResponses.findByAccessToken(accessToken);
        return new GithubUserInfoResponse(githubResponses.getEmail(), githubResponses.name());
    }

    public enum GithubResponses {
        사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
        사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
        사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
        사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

        private final String code;
        private final String accessToken;
        private final String email;

        GithubResponses(final String code, final String accessToken, final String email) {
            this.code = code;
            this.accessToken = accessToken;
            this.email = email;
        }

        public static GithubResponses findByCode(String code) {
            return Arrays.stream(GithubResponses.values())
                    .filter(it -> it.code.equals(code))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("code 정보가 유효하지 않습니다"));
        }

        public static GithubResponses findByAccessToken(String accessToken) {
            return Arrays.stream(GithubResponses.values())
                    .filter(it -> it.accessToken.equals(accessToken))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("accessToken 정보가 유효하지 않습니다"));
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
}
