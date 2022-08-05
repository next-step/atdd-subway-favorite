package nextstep.member.application.dto;

import nextstep.common.EntitySupplier;
import nextstep.member.domain.Member;

public class MemberRequest implements EntitySupplier<Member> {
    private String email;
    private String password;
    private Integer age;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public Member toEntity() {
        return new Member(email, password, age);
    }
}
