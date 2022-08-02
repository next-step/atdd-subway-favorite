package nextstep;

import java.util.List;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(admin());
        memberRepository.save(member());
    }

    private Member admin() {
        return new Member(
                "admin@subway.go.kr",
                "1q2w3e4r!",
                20,
                List.of(
                        RoleType.ROLE_MEMBER.name(),
                        RoleType.ROLE_ADMIN.name()
                )
        );
    }

    private Member member() {
        return new Member(
                "user@subway.go.kr",
                "qhdks!12",
                22,
                List.of(RoleType.ROLE_MEMBER.name())
        );
    }
}
