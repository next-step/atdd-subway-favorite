package nextstep.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.member.domain.Member;

@Getter
@AllArgsConstructor
public class OAuth2User {

    private String username;

    private String role;

    private int age;

    public Member toMember() {
        return new Member(getUsername(), "", getAge());
    }
}
