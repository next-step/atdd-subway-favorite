package nextstep.auth.domain.command;

import autoparams.AutoSource;
import nextstep.auth.domain.entity.SocialOAuthProvider;
import nextstep.fake.github.GithubStaticUsers;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AuthCommanderTest extends BaseTestSetup {

    @Autowired
    AuthCommander sut;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("authenticateEmailPassword")
    @Nested
    class AuthenticateEmailPassword {
        @ParameterizedTest
        @AutoSource
        public void sut_returns_token(Member member) {
            // given
            memberRepository.save(member);

            // when
            String actual = sut.authenticateEmailPassword(member.getEmail(), member.getPassword());

            // then
            assertThat(actual).isNotNull();
        }
    }

    @DisplayName("authenticateSocialOAuth")
    @Nested
    class AuthenticateSocialOAuth {
        @ParameterizedTest
        @AutoSource
        public void sut_returns_token(GithubStaticUsers user) {
            // given
            AuthenticateSocialOAuthCommand.ByAuthCode command = new AuthenticateSocialOAuthCommand.ByAuthCode(SocialOAuthProvider.GITHUB, user.getCode());

            // when
            String actual = sut.authenticateSocialOAuth(command);

            // then
            assertThat(actual).isNotNull();
        }
    }
}