package nextstep.api.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextstep.api.auth.domain.UserDetails;
import nextstep.api.member.domain.Member;
import nextstep.common.mapper.ModelMapperBasedObjectMapper;

/**
 * @author : Rene Choi
 * @since : 2024/02/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
public class UserPrincipal implements UserDetails {
	private String email;
	private String password;

	private Integer age;

	public static UserPrincipal of(String email){
		return UserPrincipal.builder().email(email).build();
	}

	public static UserPrincipal from(Member member) {
		return ModelMapperBasedObjectMapper.convert(member, UserPrincipal.class);
	}
}
