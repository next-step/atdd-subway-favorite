package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.member.application.dto.abstractive.MemberInterface;

import java.util.List;

@AllArgsConstructor
public class FindMemberOfMineRequest implements MemberInterface {
    private String principal;
    private List<String> roles;

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

}
