package nextstep.api.member.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.member.application.dto.MemberRequest;
import nextstep.api.member.application.dto.MemberResponse;
import nextstep.api.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse createMember(final MemberRequest request) {
        final var member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(final Long id) {
        final var member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(final String email) {
        final var member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(final Long id, final MemberRequest param) {
        final var member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(final Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponse::of)
                .orElseThrow(RuntimeException::new);
    }
}
