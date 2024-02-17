package nextstep.api.favorite.domain.model.dto.outport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextstep.api.favorite.domain.model.entity.Favorite;
import nextstep.common.mapper.ModelMapperBasedObjectMapper;

/**
 * @author : Rene Choi
 * @since : 2024/02/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteInfo {
	private Long id;
	private Long memberId;
	private Long sourceStationId;
	private Long targetStationId;

	public static FavoriteInfo from(Favorite favorite) {
		return ModelMapperBasedObjectMapper.convert(favorite, FavoriteInfo.class);
	}
}
