package nextstep.subway.utils;

import nextstep.auth.user.User;
import nextstep.member.domain.LoginMember;
import nextstep.subway.fixture.MockMember;

public class UserUtils {

    public static User createUser(MockMember member) {
        return new LoginMember(member.getEmail(), member.getPassword(), member.getAuthorities());
    }

    public static User createUserWithPassword(MockMember member, String password) {
        return new LoginMember(member.getEmail(), password, member.getAuthorities());
    }
}
