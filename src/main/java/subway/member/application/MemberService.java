package subway.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.auth.principal.UserPrincipal;
import subway.auth.userdetails.UserDetails;
import subway.constant.SubwayMessage;
import subway.exception.SubwayNotFoundException;
import subway.member.application.dto.MemberRequest;
import subway.member.application.dto.MemberResponse;
import subway.member.domain.Member;
import subway.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.to());
        return MemberResponse.from(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.MEMBER_NOT_FOUND));
        return MemberResponse.from(member);
    }

    public MemberResponse findMember(UserPrincipal principal) {
        Member member = memberRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.MEMBER_NOT_FOUND));
        return MemberResponse.from(member);
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

    public MemberResponse findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponse::from)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.MEMBER_NOT_FOUND));
    }
}