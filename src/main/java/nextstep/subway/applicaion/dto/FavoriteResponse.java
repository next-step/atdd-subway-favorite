package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteResponse {
	private Long id;
	private StationResponse source;
	private StationResponse target;
}
