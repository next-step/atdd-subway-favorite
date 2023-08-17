package nextstep.member.unit.fixture;

import nextstep.member.domain.Member;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    private static Long id = 1L;

    public static Member 회원_생성(String email, String password, int age) {
        Member member = new Member(email, password, age);
        ReflectionTestUtils.setField(member, "id", id++);
        return member;
    }
}
