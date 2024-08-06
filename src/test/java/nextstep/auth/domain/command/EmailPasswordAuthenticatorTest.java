package nextstep.auth.domain.command;

import autoparams.AutoSource;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class EmailPasswordAuthenticatorTest extends BaseTestSetup {

    @Autowired
    EmailPasswordAuthenticator sut;

    @Autowired
    MemberRepository memberRepository;

    @ParameterizedTest
    @AutoSource
    public void sut_returns_token(Member member) {
        // given
        memberRepository.save(member);

        // when
        String actual = sut.authenticate(member.getEmail(), member.getPassword());

        // then
        assertThat(actual).isNotNull();
    }

}