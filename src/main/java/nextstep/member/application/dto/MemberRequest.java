package nextstep.member.application.dto;

import nextstep.common.EntitySupplier;
import nextstep.member.domain.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MemberRequest implements EntitySupplier<Member> {
    @NotNull
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotNull
    @Min(1)
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
