package nextstep.member.fixture;

import java.util.HashMap;
import java.util.Map;
import nextstep.member.domain.Member;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    public static Map<String, Object> 회원_생성_요청_본문(String email, String password, int age) {
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("age", age);
        return body;
    }

    public static Member giveOne(long id, String email, String password, Integer age) {
        Member member = new Member(email, password, age);
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }
}
