package nextstep.member.fixture;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.Member;

public class MemberFixture {

    public static final MemberRequest 회원_정보_DTO = MemberRequest.builder()
            .email("test@gmail.com")
            .password("testpassword123")
            .age(27)
            .build();

    public static Member 회원_정보_엔티티 = 회원_정보_DTO.toEntity();

}
