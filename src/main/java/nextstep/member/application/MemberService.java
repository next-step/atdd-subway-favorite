package nextstep.member.application;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(AuthenticationException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(AuthenticationException::new);
        member.update(param.toMember());
    }

//    public void updateMember(LoginMember loginMember) {
//        Member member = memberRepository.findById(lo).orElseThrow(RuntimeException::new);
//        member.update(param.toMember());
//    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
