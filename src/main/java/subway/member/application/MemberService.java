package subway.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.auth.principal.UserPrincipal;
import subway.constant.SubwayMessage;
import subway.exception.SubwayNotFoundException;
import subway.member.application.dto.MemberRequest;
import subway.member.application.dto.MemberRetrieveResponse;
import subway.member.domain.Member;
import subway.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberRetrieveResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toInit());
        return MemberRetrieveResponse.from(member);
    }

    public MemberRetrieveResponse findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.MEMBER_NOT_FOUND));
        return MemberRetrieveResponse.from(member);
    }

    public MemberRetrieveResponse findMember(UserPrincipal principal) {
        final String email = principal.getUsername();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.MEMBER_NOT_FOUND));
        return MemberRetrieveResponse.from(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.MEMBER_NOT_FOUND));
        member.update(param.to());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberRetrieveResponse findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberRetrieveResponse::from)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.MEMBER_NOT_FOUND));
    }
}