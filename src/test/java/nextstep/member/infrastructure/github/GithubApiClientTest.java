package nextstep.member.infrastructure.github;

import autoparams.AutoSource;
import nextstep.fake.github.GithubStaticUsers;
import nextstep.member.infrastructure.github.dto.GithubProfileResponse;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class GithubApiClientTest extends BaseTestSetup {

    @Autowired
    GithubApiClient sut;

    @Autowired
    GithubConfig config;

    @DisplayName("getUser")
    @Nested
    class GetAccessToken {
        @ParameterizedTest
        @AutoSource
        public void sut_returns_users(GithubStaticUsers expected) {
            // when
            GithubProfileResponse actual = sut.getUser(expected.getAccessToken());

            // then
            assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        }
    }
}