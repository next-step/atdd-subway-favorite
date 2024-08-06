package nextstep.member.domain.command;

import autoparams.AutoSource;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.auth.domain.entity.TokenPrincipal;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MemberSocialOAuthUserVerifierTest extends BaseTestSetup {

    @Autowired
    MemberSocialOAuthUserVerifier sut;

    @Autowired
    MemberRepository memberRepository;

    @ParameterizedTest
    @AutoSource
    public void sut_create_member_if_not_found(SocialOAuthUser user) {
        // when
        sut.verify(user);

        // then
        Member actual = memberRepository.findByEmailOrThrow(user.getEmail());
        assertThat(actual.getId()).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    public void sut_returns_token_principal(SocialOAuthUser user) {
        // given
        Member expected = new Member(user.getEmail(), UUID.randomUUID().toString(), null);
        memberRepository.save(expected);

        // when
        TokenPrincipal actual = sut.verify(user);

        // then
        assertThat(actual.getSubject()).isEqualTo(expected.getId());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }
}