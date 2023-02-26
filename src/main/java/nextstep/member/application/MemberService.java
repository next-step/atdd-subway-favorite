package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.exception.NotAuthorizedException;
import nextstep.member.domain.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional
    public Member findOrCreateMember(String email) {
        return memberRepository.findByEmail(email)
            .orElseGet(() -> memberRepository.save(new Member(email)));
    }

    public MemberResponse findMember(Long id) {
        return MemberResponse.of(findById(id));
    }

    public MemberResponse findMember(String principal) {
        return MemberResponse.of(findByEmail(principal));
    }

    public Member findByEmail(String principal) {
        return memberRepository.findByEmail(principal)
            .orElseThrow(() -> new NotAuthorizedException(principal + " 사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = findById(id);
        member.update(param.toMember());
    }

    private Member findById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(id + " 번 사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
