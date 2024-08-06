package nextstep.auth.infrastructure.composite;

import autoparams.AutoSource;
import nextstep.auth.domain.infrastructure.composite.CompositeSocialOAuthAuthenticator;
import nextstep.fake.github.GithubStaticUsers;
import nextstep.auth.domain.command.AuthenticateSocialOAuthCommand;
import nextstep.auth.domain.entity.SocialOAuthProvider;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CompositeSocialOAuthAuthenticatorTest extends BaseTestSetup {

    @Autowired
    CompositeSocialOAuthAuthenticator sut;

    @ParameterizedTest
    @AutoSource
    public void sut_returns_github_user_if_provider_github(GithubStaticUsers expected) {
        // given
        AuthenticateSocialOAuthCommand.ByAuthCode command = new AuthenticateSocialOAuthCommand.ByAuthCode(SocialOAuthProvider.GITHUB, expected.getCode());

        // when
        SocialOAuthUser actual = sut.authenticate(command);

        // then
        assertThat(actual.getProvider()).isEqualTo(SocialOAuthProvider.GITHUB);
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }
}