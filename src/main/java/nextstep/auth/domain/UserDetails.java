package nextstep.auth.domain;

import lombok.Builder;
import lombok.Getter;
import nextstep.member.domain.Member;

@Builder
@Getter
public class UserDetails {
    private final String principal;
    private final String credential;

    public static UserDetails from(Member member) {
        return UserDetails.builder()
                .principal(member.getEmail())
                .credential(member.getPassword())
                .build();
    }
}
