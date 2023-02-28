package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("test")
public class TestDataLoader implements DataLoader {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void loadData() {
        createMembers();
    }

    private void createMembers() {
        memberRepository.save(new Member("admin@email.com", "password", 20, List.of(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("email@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
