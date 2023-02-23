package nextstep.member.application;

import nextstep.exception.AuthenticationTokenException;
import nextstep.exception.MemberInvalidException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private static final String BEARER = "Bearer ";
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

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberByToken(String accessToken) {
        String token = parseAccessToken(accessToken);
        boolean validateToken = jwtTokenProvider.validateToken(token);
        if (!validateToken) {
            throw new AuthenticationTokenException();
        }
        String principal = jwtTokenProvider.getPrincipal(token);
        Member member = memberRepository.findByEmail(principal)
                .orElseThrow((MemberInvalidException::new));
        return MemberResponse.of(member);
    }

    private String parseAccessToken(String accessToken) {
        if (accessToken.startsWith(BEARER)) {
            return accessToken.substring(7);
        }
        throw new AuthenticationTokenException();
    }
}