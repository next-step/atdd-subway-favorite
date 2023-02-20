package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DataLoader { // 이렇게 하는 이유가 뭘까? 테스트코드에서 @BeforeEach로 직접 넣는거랑 차이가 있나?

    private final MemberRepository memberRepository;

    public void loadData() {
        memberRepository.save(createMember("admin@email.com", RoleType.ROLE_ADMIN));
        memberRepository.save(createMember("member@email.com", RoleType.ROLE_MEMBER));
    }

    private Member createMember(String email, RoleType roleAdmin) {
        return new Member(email, "password", 20, List.of(roleAdmin.name()));
    }
}
