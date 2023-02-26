package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import static nextstep.common.constants.ErrorConstant.NOT_FOUND_EMAIL;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = saveMember(request);
        return MemberResponse.of(member);
    }

    public Member saveMember(final MemberRequest request) {
        return memberRepository.save(request.toMember());
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new IllegalArgumentException(NOT_FOUND_EMAIL);
        });
    }

    public MemberResponse findMemberByEmail(final String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new IllegalArgumentException(NOT_FOUND_EMAIL);
        });
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}