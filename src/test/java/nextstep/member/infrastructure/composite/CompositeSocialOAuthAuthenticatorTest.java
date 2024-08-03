package nextstep.member.infrastructure.composite;

import autoparams.AutoSource;
import nextstep.fake.github.GithubStaticUsers;
import nextstep.member.domain.command.authenticator.SocialOAuthAuthenticateCommand;
import nextstep.member.domain.command.authenticator.SocialOAuthProvider;
import nextstep.member.domain.command.authenticator.SocialOAuthUser;
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
        SocialOAuthAuthenticateCommand.ByAuthCode command = new SocialOAuthAuthenticateCommand.ByAuthCode(SocialOAuthProvider.GITHUB, expected.getCode());

        // when
        SocialOAuthUser actual = sut.authenticate(command);

        // then
        assertThat(actual.getProvider()).isEqualTo(SocialOAuthProvider.GITHUB);
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }
}