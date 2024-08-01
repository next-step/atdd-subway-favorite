package nextstep.member.domain.query;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberReader {
    private final MemberRepository memberRepository;

    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Member getMe(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new);
    }
}