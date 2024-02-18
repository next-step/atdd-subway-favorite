package nextstep.api.member.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class LoginMember implements MemberDetails {
	String email;

}
