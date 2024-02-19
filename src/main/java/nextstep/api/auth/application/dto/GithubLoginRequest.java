package nextstep.api.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextstep.api.auth.domain.dto.inport.GithubCodeResponse;
import nextstep.common.mapper.ModelMapperBasedObjectMapper;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GithubLoginRequest {
	private String code;

	public static GithubLoginRequest from(GithubCodeResponse codeResponse){
		return ModelMapperBasedObjectMapper.convert(codeResponse, GithubLoginRequest.class);
	}
}
