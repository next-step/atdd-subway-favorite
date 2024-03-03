package nextstep.subway.fixture;

import java.util.HashMap;
import java.util.Map;

public class MemberFixture {

    public static Map<String, Object> 회원_생성_요청_본문(String email, String password, int age) {
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("age", age);
        return body;
    }
}
