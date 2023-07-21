package subway.member.application;

import subway.member.application.dto.MemberRequest;
import subway.member.domain.Member;
import subway.member.domain.MemberRepository;
import subway.member.application.dto.MemberResponse;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.to());
        return MemberResponse.from(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.from(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.to());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponse::from)
                .orElseThrow(RuntimeException::new);
    }
}