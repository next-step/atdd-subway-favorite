package nextstep;

import com.google.common.collect.Lists;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, Lists.newArrayList(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password2", 17, Lists.newArrayList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("other@email.com", "password123", 33, Lists.newArrayList(RoleType.ROLE_MEMBER.name())));

    }
}
