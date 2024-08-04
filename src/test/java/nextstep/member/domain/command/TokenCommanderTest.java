package nextstep.member.domain.command;

import autoparams.AutoSource;
import nextstep.base.exception.AuthenticationException;
import nextstep.fake.github.GithubStaticUsers;
import nextstep.member.domain.command.authenticator.AuthenticateSocialOAuthCommand;
import nextstep.member.domain.command.authenticator.SocialOAuthProvider;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.exception.MemberDomainException;
import nextstep.member.domain.exception.MemberDomainExceptionType;
import nextstep.member.domain.repository.MemberRepository;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class TokenCommanderTest extends BaseTestSetup {

    @Autowired
    TokenCommander sut;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("createToken")
    @Nested
    class CreateToken {
        @ParameterizedTest
        @AutoSource
        public void sut_throws_if_not_found_member(Member member) {
            // when
            MemberDomainException actual = (MemberDomainException) catchThrowable(() -> sut.createToken(member.getEmail(), member.getPassword()));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(MemberDomainExceptionType.NOT_FOUND_MEMBER);
        }

        @ParameterizedTest
        @AutoSource
        public void sut_throws_if_password_invalid(Member member) {
            // given
            memberRepository.save(member);

            // when
            AuthenticationException actual = (AuthenticationException) catchThrowable(() -> sut.createToken(member.getEmail(), UUID.randomUUID().toString()));

            // then
            assertThat(actual.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(actual.getName()).isEqualTo("UNAUTHORIZED");
        }

        @ParameterizedTest
        @AutoSource
        public void sut_returns_token(Member member) {
            // given
            memberRepository.save(member);

            // when
            String actual = sut.createToken(member.getEmail(), member.getPassword());

            // then
            assertThat(actual).isNotNull();
        }
    }

    @DisplayName("authenticateSocialOAuth")
    @Nested
    class AuthenticateSocialOAuth {
        @ParameterizedTest
        @AutoSource
        public void sut_create_member_if_not_found(GithubStaticUsers user) {
            // given
            AuthenticateSocialOAuthCommand.ByAuthCode command = new AuthenticateSocialOAuthCommand.ByAuthCode(SocialOAuthProvider.GITHUB, user.getCode());

            // when
            sut.authenticateSocialOAuth(command);

            // then
            Member actual = memberRepository.findByEmailOrThrow(user.getEmail());
            assertThat(actual.getId()).isNotNull();
        }

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