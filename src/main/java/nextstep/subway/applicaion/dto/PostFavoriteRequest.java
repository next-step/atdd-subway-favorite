package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFavoriteRequest {
	Long source;
	Long target;

	public PostFavoriteRequest(Long source, Long target) {
		this.source = source;
		this.target = target;
	}
}
