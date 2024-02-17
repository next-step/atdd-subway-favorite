package nextstep.api.favorite.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextstep.api.favorite.domain.model.dto.outport.FavoriteCreateInfo;

/**
 * @author : Rene Choi
 * @since : 2024/02/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteCreateResponse {
	private Long id;

	public static FavoriteCreateResponse from(FavoriteCreateInfo favoriteCreateInfo) {
		return FavoriteCreateResponse.builder().id(favoriteCreateInfo.getId()).build();
	}
}
