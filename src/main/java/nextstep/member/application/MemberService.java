package nextstep.member.application;

import nextstep.exception.BadCredentialException;
import nextstep.exception.InvalidAccessTokenException;
import nextstep.exception.MemberNotFoundException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(String authorization) {
        String accessToken = authorization.split(" ")[1];

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new InvalidAccessTokenException(accessToken);
        }

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(accessToken));
        return findMember(id);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member authenticate(String email, String password) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException(email));

        if (!member.checkPassword(password)) {
            throw new BadCredentialException();
        }

        return member;
    }
}
