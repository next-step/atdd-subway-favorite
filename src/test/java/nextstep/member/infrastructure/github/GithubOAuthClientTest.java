package nextstep.member.infrastructure.github;

import autoparams.AutoSource;
import nextstep.fake.github.GithubStaticUsers;
import nextstep.member.infrastructure.github.dto.GithubAccessTokenRequest;
import nextstep.member.infrastructure.github.dto.GithubAccessTokenResponse;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class GithubOAuthClientTest extends BaseTestSetup {

    @Autowired
    GithubOAuthClient sut;

    @Autowired
    GithubConfig config;

    @DisplayName("getAccessToken")
    @Nested
    class GetAccessToken {
        @ParameterizedTest
        @AutoSource
        public void sut_returns_access_token() {
            // given
            GithubAccessTokenRequest request = new GithubAccessTokenRequest(config.getClientId(), config.getClientSecret(), GithubStaticUsers.SAMPLE1.getCode());

            // when
            GithubAccessTokenResponse actual = sut.getAccessToken(request);

            // then
            assertThat(actual.getAccessToken()).isEqualTo(GithubStaticUsers.SAMPLE1.getAccessToken());
        }
    }
}