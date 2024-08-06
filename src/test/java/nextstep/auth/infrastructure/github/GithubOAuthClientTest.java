package nextstep.auth.infrastructure.github;

import autoparams.AutoSource;
import nextstep.auth.domain.infrastructure.github.GithubConfig;
import nextstep.auth.domain.infrastructure.github.GithubOAuthClient;
import nextstep.fake.github.GithubStaticUsers;
import nextstep.auth.domain.infrastructure.github.dto.GithubAccessTokenRequest;
import nextstep.auth.domain.infrastructure.github.dto.GithubAccessTokenResponse;
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
        public void sut_returns_access_token(GithubStaticUsers expected) {
            // given
            GithubAccessTokenRequest request = new GithubAccessTokenRequest(config.getClientId(), config.getClientSecret(), expected.getCode());

            // when
            GithubAccessTokenResponse actual = sut.getAccessToken(request);

            // then
            assertThat(actual.getAccessToken()).isEqualTo(expected.getAccessToken());
        }
    }
}