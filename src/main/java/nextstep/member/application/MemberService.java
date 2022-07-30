package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.NotFoundMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
        member.update(param.toMember());
    }

    @Transactional
    public void updateMember(String email, MemberRequest param) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional
    public void deleteMember(String email) {
        memberRepository.deleteByEmail(email);
    }
}