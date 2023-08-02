package nextstep.member.fixture;

import nextstep.member.dto.MemberRequest;
import nextstep.member.entity.Member;

public class MemberFixture {

    public static final MemberRequest 회원_정보_DTO = MemberRequest.builder()
            .email("test@gmail.com")
            .password("testpassword123")
            .age(27)
            .build();

    public static Member 회원_정보_엔티티 = Member.builder()
            .email("test@gmail.com")
            .password("testpassword123")
            .age(27)
            .build();

}
