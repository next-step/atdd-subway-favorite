package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

@Getter
@NoArgsConstructor
public class FavoriteResponse {
	Long id;
	Station source;
	Station target;

	@Builder
	public FavoriteResponse(Long id, Station source, Station target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Favorite favorite) {
		return FavoriteResponse.builder()
				.id(favorite.getId())
				.source(favorite.getSource())
				.target(favorite.getTarget())
				.build();
	}
}
