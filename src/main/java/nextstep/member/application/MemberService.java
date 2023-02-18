package nextstep.member.application;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import nextstep.exception.NotFoundMemberException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

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

    public MemberResponse findByAccessToken(String accessToken) {
        Preconditions.checkArgument(jwtTokenProvider.validateToken(accessToken),
            "Invalid Access Token: %s", accessToken);

        String email = jwtTokenProvider.getPrincipal(accessToken);

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(NotFoundMemberException::new);

        return MemberResponse.of(member);
    }
}
