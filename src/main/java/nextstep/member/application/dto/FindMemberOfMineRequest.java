package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindMemberOfMineRequest {
    private String principal;
    private List<String> roles;
}
