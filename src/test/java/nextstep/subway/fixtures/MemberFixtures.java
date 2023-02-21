package nextstep.subway.fixtures;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class MemberFixtures {

    public static final Long ADMIN_ID = 1L;
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";

    public static final String EMAIL_2 = "member@email.com";
    public static final String PASSWORD_2 = "password";

    public static final String INVALID_ACCESS_TOKEN = "yJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZdsfifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y";

    public static Member 어드민_유저() {
        Member member = new Member(EMAIL, PASSWORD);
        setId(member, ADMIN_ID);

        return member;
    }

    private static void setId(Member member, Long id) {
        Field idField = ReflectionUtils.findField(member.getClass(), "id");

        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(idField, member, id);
    }
}
