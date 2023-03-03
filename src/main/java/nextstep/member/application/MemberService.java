package nextstep.member.application;

import nextstep.common.exception.EntityNotFoundException;
import nextstep.common.exception.InvalidUserInfoException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public Member findOrCreateMember(String principal, String code) {
        return memberRepository.findByEmail(principal).orElseGet(() -> memberRepository.save(new Member(principal, code)));
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByEmail(String principal) {
        Member member = memberRepository.findByEmail(principal).orElseThrow(() -> new EntityNotFoundException(principal));
        return MemberResponse.of(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(InvalidUserInfoException::new);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}