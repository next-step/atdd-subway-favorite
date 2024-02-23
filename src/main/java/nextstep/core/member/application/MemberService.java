package nextstep.core.member.application;

import nextstep.core.member.application.dto.MemberRequest;
import nextstep.core.member.application.dto.MemberResponse;
import nextstep.core.member.domain.LoginMember;
import nextstep.core.member.domain.Member;
import nextstep.core.member.domain.MemberRepository;
import nextstep.core.member.exception.NotFoundMemberException;
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

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException("회원 정보가 없습니다."));
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException("회원 정보가 없습니다."));
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundMemberException("회원 정보가 없습니다."));
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(MemberResponse::of)
                .orElseThrow(() -> new NotFoundMemberException("회원 정보가 없습니다."));
    }
}