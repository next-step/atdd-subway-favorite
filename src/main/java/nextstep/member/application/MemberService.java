package nextstep.member.application;

import nextstep.member.application.dto.LoginMemberRequest;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.exception.InvalidTokenException;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(final MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(final Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public LoginMemberRequest findMember(final String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }
        String principal = jwtTokenProvider.getPrincipal(token);
        Member member = memberRepository.findByEmail(principal).orElseThrow(MemberNotFoundException::new);
        return new LoginMemberRequest(member.getId(), member.getRoles());
    }

    public void updateMember(final Long id, final MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(final Long id) {
        memberRepository.deleteById(id);
    }
}
