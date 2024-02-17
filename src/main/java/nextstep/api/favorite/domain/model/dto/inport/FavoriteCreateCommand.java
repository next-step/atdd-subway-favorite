package nextstep.api.favorite.domain.model.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextstep.api.favorite.application.model.dto.FavoriteCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/02/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteCreateCommand {
	private Long id;
	private Long sourceStationId;
	private Long targetStationId;
	private Long memberId;

	public static FavoriteCreateCommand of(FavoriteCreateRequest request, Long memberId) {
		return FavoriteCreateCommand.builder()
			.id(request.getId())
			.sourceStationId(request.getSourceStationId())
			.targetStationId(request.getTargetStationId())
			.memberId(memberId)
			.build();
	}
}
