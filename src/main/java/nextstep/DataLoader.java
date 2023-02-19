package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {

    @Autowired
    private MemberRepository memberRepository;

    public void loadData() {
        createMembers();
    }

    private void createMembers() {
        memberRepository.save(new Member("admin@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
