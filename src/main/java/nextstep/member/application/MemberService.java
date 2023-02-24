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
    public Member createMember(String email) {
        return memberRepository.save(new Member(email));
    }

    public MemberResponse findMember(Long id) {
        return MemberResponse.of(findById(id));
    }

    public MemberResponse findMember(String principal) {
        if (principal == null) {
            throw new NotAuthorizedException("인증정보가 유효하지 않습니다.");
        }

        return MemberResponse.of(findByUserEmail(principal));
    }

    public Member findByUserEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(email + " 사용자를 찾을 수 없습니다."));
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
