package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DataLoader { // 이렇게 하는 이유가 뭘까? 테스트코드에서 @BeforeEach로 직접 넣는거랑 차이가 있나? 굳이 테스트에서 써야할까?


    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final String ACCESS_TOKEN = GithubResponses.사용자1.getAccessToken();
    public static final int AGE = 20;

    private final MemberRepository memberRepository;

    public void loadData() {
        memberRepository.save(createMember(EMAIL, RoleType.ROLE_ADMIN, ACCESS_TOKEN));
        memberRepository.save(createMember("member@email.com", RoleType.ROLE_MEMBER, ACCESS_TOKEN + 1));
    }

    private Member createMember(String email, RoleType roleAdmin, String accessToken) {
        return new Member(email, PASSWORD, AGE, accessToken, List.of(roleAdmin.name()));
    }
}
