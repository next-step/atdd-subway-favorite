package nextstep;

import lombok.AllArgsConstructor;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader {

    private final MemberRepository memberRepository;

    public void loadData() {
        memberRepository.save(MemberData.admin);
        memberRepository.save(MemberData.member);
    }
}
