package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        return MemberResponse.of(findById(id));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member authenticate(final TokenRequest tokenRequest) {
        final Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        member.checkPassword(tokenRequest.getPassword());

        return member;
    }

    public Member findByEmailOrCreate(final String email) {
        return memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email)));
    }
}