package nextstep.auth.domain.command;

import autoparams.AutoSource;
import nextstep.auth.domain.entity.SocialOAuthProvider;
import nextstep.fake.github.GithubStaticUsers;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class SocialOAuthUserAuthenticatorTest extends BaseTestSetup {

    @Autowired
    SocialOAuthUserAuthenticator sut;

    @ParameterizedTest
    @AutoSource
    public void sut_returns_token(GithubStaticUsers user) {
        // given
        AuthenticateSocialOAuthCommand.ByAuthCode command = new AuthenticateSocialOAuthCommand.ByAuthCode(SocialOAuthProvider.GITHUB, user.getCode());

        // when
        String actual = sut.authenticate(command);

        // then
        assertThat(actual).isNotNull();
    }
}