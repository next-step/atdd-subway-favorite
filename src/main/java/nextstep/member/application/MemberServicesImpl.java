package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static nextstep.common.constant.ErrorCode.MEMBER_NOT_FOUND;

@Service
public class MemberServicesImpl implements MemberServices {

    private final MemberRepository memberRepository;

    public MemberServicesImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberResponse createMember(final MemberRequest memberRequest) {
        Member member = memberRepository.save(memberRequest.toMember());
        return MemberResponse.of(member);
    }

    @Override
    public MemberResponse findMember(final Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    @Override
    public void updateMember(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(memberRequest.toMember());
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(String.valueOf(MEMBER_NOT_FOUND)));
    }

    @Override
    public Optional<Member> findMemberOptionalByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(it -> MemberResponse.of(it))
                .orElseThrow(RuntimeException::new);
    }
}

