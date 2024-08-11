package nextstep.converter;

import nextstep.auth.application.dto.AuthMember;
import nextstep.member.domain.Member;

public class MemberConverter {

    private MemberConverter () {
        throw new UnsupportedOperationException("Utility class");
    }

    public static AuthMember MemberToAuthMember(Member member) {
        return AuthMember.of(member.getEmail(), member.getPassword());
    }
}
