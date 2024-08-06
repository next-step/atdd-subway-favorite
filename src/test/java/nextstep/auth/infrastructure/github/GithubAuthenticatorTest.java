package nextstep.auth.infrastructure.github;

import autoparams.AutoSource;
import nextstep.auth.domain.infrastructure.github.GithubAuthenticator;
import nextstep.fake.github.GithubStaticUsers;
import nextstep.auth.domain.command.AuthenticateSocialOAuthCommand;
import nextstep.auth.domain.entity.SocialOAuthProvider;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class GithubAuthenticatorTest extends BaseTestSetup {

    @Autowired
    GithubAuthenticator sut;

    @ParameterizedTest
    @AutoSource
    public void sut_returns_social_user(GithubStaticUsers githubStaticUsers) {
        // given
        AuthenticateSocialOAuthCommand.ByAuthCode command = new AuthenticateSocialOAuthCommand.ByAuthCode(SocialOAuthProvider.GITHUB, githubStaticUsers.getCode());

        // when
        SocialOAuthUser actual = sut.authenticate(command);

        // then
        assertThat(actual.getProvider()).isEqualTo(SocialOAuthProvider.GITHUB);
        assertThat(actual.getEmail()).isEqualTo(githubStaticUsers.getEmail());
    }
}