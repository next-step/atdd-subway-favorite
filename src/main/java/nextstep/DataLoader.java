package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final MemberRepository memberRepository;

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, List.of(RoleType.ROLE_ADMIN.name())));
    }
}
