package nextstep.member.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private static final String BEARER_PREFIX = "Bearer ";
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
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

    public Member findMemberByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmail(email).filter(it -> it.checkPassword(password)).orElseThrow(RuntimeException::new);
    }

    public MemberResponse findMemberOfMine(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException();
        }

        String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException();
        }

        String email = jwtTokenProvider.getPrincipal(accessToken);

        return MemberResponse.of(memberRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new));
    }
}