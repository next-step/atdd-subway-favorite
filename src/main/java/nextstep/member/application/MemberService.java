package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse findMemberById(final Long id) {
        Member member = findById(id);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByEmail(final String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public MemberResponse createMember(final MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(final Long id, final MemberRequest param) {
        Member member = findById(id);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(final Long id) {
        memberRepository.deleteById(id);
    }


    private Member findById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }
}
