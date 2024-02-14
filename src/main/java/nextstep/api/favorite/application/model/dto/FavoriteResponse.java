package nextstep.api.favorite.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextstep.api.subway.interfaces.dto.response.StationResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
public class FavoriteResponse {
	private Long id;
	private StationResponse source;
	private StationResponse target;

}
