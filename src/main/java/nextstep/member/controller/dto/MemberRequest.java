package nextstep.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.member.domain.command.MemberCommand;

@Getter
@AllArgsConstructor
public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public MemberRequest() {
    }

    public MemberCommand.CreateMember toCreateCommand() {
        return new MemberCommand.CreateMember(email, password, age);
    }

    public MemberCommand.UpdateMember toUpdateCommand(Long id) {
        return new MemberCommand.UpdateMember(id, email, password, age);
    }
}
