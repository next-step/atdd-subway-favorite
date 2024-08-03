package nextstep.subway.member.application;

import nextstep.subway.exception.NoSuchLineException;
import nextstep.subway.exception.NoSuchMemberException;
import nextstep.subway.member.application.dto.MemberRequest;
import nextstep.subway.member.application.dto.MemberResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchMemberException("존재하지 않는 사용자입니다."));
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchMemberException("존재하지 않는 사용자입니다."));
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmailOrThrow(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchMemberException("존재하지 않는 사용자입니다."));
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(it -> MemberResponse.of(it))
                .orElseThrow(() -> new NoSuchMemberException("존재하지 않는 사용자입니다."));
    }
}