package nextstep.member.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MemberUpdateRequest {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Min(1)
    private Integer age;

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
