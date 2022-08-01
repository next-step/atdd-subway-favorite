package nextstep;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final MemberRepository memberRepository;

    public void loadData() {
        Member member = new Member("admin", "password", 30, List.of("ROLE_ADMIN", "ROLE_MEMBER"));
        memberRepository.save(member);
    }
}
