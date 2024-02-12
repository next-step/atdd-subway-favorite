package nextstep.api.subway.domain.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.api.subway.interfaces.dto.request.SectionCreateRequest;
import nextstep.common.mapper.ModelMapperBasedObjectMapper;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionCreateCommand {

	private Long downStationId;
	private Long upStationId;
	private Long distance;

	public static SectionCreateCommand from(SectionCreateRequest createRequest){
		return ModelMapperBasedObjectMapper.convert(createRequest, SectionCreateCommand.class);
	}
}
