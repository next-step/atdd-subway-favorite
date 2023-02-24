package nextstep.utils;

import nextstep.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static nextstep.fixture.MemberFixture.관리자_ADMIN;
import static nextstep.fixture.MemberFixture.회원_ALEX;

@Component
public class DataLoader {

    @Autowired
    private MemberRepository memberRepository;

    public void loadData() {
        memberRepository.save(회원_ALEX.엔티티_생성());
        memberRepository.save(관리자_ADMIN.엔티티_생성());
    }
}
