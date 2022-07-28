package nextstep.member.application.dto;

import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;

import java.util.List;

public class MemberRequest {
    private String email;
    private String password;
    private Integer age;
    private boolean admin;

    public MemberRequest() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public Member toMember() {
        if (admin) {
            return new Member(email, password, age, List.of(RoleType.ROLE_MEMBER.name(), RoleType.ROLE_ADMIN.name()));
        }
        return new Member(email, password, age);
    }
}
