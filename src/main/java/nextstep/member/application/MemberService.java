package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.MemberUpdateRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toEntity());
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(String email) {
        return findByEmail(email);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberResponse(String email) {
        Member member = findByEmail(email);
        return MemberResponse.of(member);
    }

    public void updateMember(String email, MemberUpdateRequest param) {
        Member member = findByEmail(email);
        member.update(param.getEmail(), param.getAge());
    }

    public void deleteMember(String email) {
        memberRepository.deleteByEmail(email);
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
    }

    void saveAndFlush(Member member) {
        memberRepository.saveAndFlush(member);
    }
}