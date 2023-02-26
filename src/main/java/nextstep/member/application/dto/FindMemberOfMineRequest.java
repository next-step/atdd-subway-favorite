package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import nextstep.member.application.dto.abstractive.MemberProvider;

import java.util.List;

@AllArgsConstructor
public class FindMemberOfMineRequest implements MemberProvider {
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
