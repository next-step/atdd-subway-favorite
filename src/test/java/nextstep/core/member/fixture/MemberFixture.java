package nextstep.core.member.fixture;

import nextstep.core.member.domain.Member;

public class MemberFixture {
    public static Member 회원_생성(String 이메일, String 비밀번호, int 나이) {
        return new Member(이메일, 비밀번호, 나이);
    }
}
