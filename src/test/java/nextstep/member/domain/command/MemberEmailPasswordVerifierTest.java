package nextstep.member.domain.command;

import autoparams.AutoSource;
import nextstep.auth.domain.entity.TokenPrincipal;
import nextstep.base.exception.AuthenticationException;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.exception.MemberDomainException;
import nextstep.member.domain.exception.MemberDomainExceptionType;
import nextstep.member.domain.repository.MemberRepository;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class MemberEmailPasswordVerifierTest extends BaseTestSetup {

    @Autowired
    MemberEmailPasswordVerifier sut;

    @Autowired
    MemberRepository memberRepository;

    @ParameterizedTest
    @AutoSource
    public void sut_throws_if_not_found_member(Member member) {
        // when
        MemberDomainException actual = (MemberDomainException) catchThrowable(() -> sut.verify(member.getEmail(), member.getPassword()));

        // then
        assertThat(actual.getExceptionType()).isEqualTo(MemberDomainExceptionType.NOT_FOUND_MEMBER);
    }

    @ParameterizedTest
    @AutoSource
    public void sut_throws_if_password_invalid(Member member) {
        // given
        memberRepository.save(member);

        // when
        AuthenticationException actual = (AuthenticationException) catchThrowable(() -> sut.verify(member.getEmail(), UUID.randomUUID().toString()));

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(actual.getName()).isEqualTo("UNAUTHORIZED");
    }

    @ParameterizedTest
    @AutoSource
    public void sut_returns_token_principal(Member member) {
        // given
        memberRepository.save(member);

        // when
        TokenPrincipal actual = sut.verify(member.getEmail(), member.getPassword());

        // then
        assertThat(actual.getSubject()).isEqualTo(member.getId());
        assertThat(actual.getEmail()).isEqualTo(member.getEmail());
    }
}