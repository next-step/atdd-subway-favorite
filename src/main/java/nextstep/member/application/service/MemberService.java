package nextstep.member.application.service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.domain.CustomUserPrincipal;
import nextstep.common.error.exception.NotFoundException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(NotFoundException::new);
    }

    public MemberResponse findMe(CustomUserPrincipal customUserDetail) {
        return memberRepository.findByEmail(customUserDetail.getId())
            .map(MemberResponse::of)
            .orElseThrow(NotFoundException::new);
    }
}
