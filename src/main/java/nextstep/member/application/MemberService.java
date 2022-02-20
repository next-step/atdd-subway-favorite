package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public void deleteMember(String email) {
        memberRepository.deleteByEmail(email);
    }

    public MemberResponse findMemberByEmail(String email) {
        final Member foundMember = getFoundMember(email);
        return MemberResponse.of(foundMember);
    }

    public MemberResponse updateMember(String email, MemberRequest memberRequest) {
        final Member foundMember = getFoundMember(email);
        foundMember.update(memberRequest.toMember());
        return MemberResponse.of(foundMember);
    }

    private Member getFoundMember(String email) {
        final Member foundMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 멤버를 찾을 수 없습니다."));
        return foundMember;
    }
}