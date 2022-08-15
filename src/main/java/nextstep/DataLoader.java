package nextstep;

import lombok.AllArgsConstructor;
import nextstep.member.application.LoginMemberService;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader {

    private final MemberRepository memberRepository;

    public void loadData() {
        memberRepository.save(MemberData.admin);
        memberRepository.save(MemberData.member);
    }
}
