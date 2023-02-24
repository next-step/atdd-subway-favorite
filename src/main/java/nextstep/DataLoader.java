package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {
    private final MemberRepository memberRepository;
    private List<Member> members = new ArrayList<>();
    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        members.stream()
                .forEach(member -> memberRepository.save(member));
    }

    public void addMember(Member member) {
        members.add(member);
    }
}
