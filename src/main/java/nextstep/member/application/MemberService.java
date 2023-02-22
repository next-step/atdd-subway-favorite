package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.exception.NotAuthorizedException;
import nextstep.member.domain.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Member createMember(String email) {
        return memberRepository.save(Member.of(email));
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(String principal) {
        if (principal == null) {
            throw new NotAuthorizedException("인증정보가 유효하지 않습니다.");
        }

        Member member = findByUserEmail(principal).orElseThrow(() -> new NotFoundException(principal + " 사용자를 찾을 수 없습니다."));
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id + " 번 사용자를 찾을 수 없습니다."));
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Optional<Member> findByUserEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
