package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {

	private long id;
	private StationResponse source;
	private StationResponse target;

	private FavoriteResponse() {
	}

	public FavoriteResponse(long id, StationResponse source, StationResponse target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(
				favorite.getId(),
				StationResponse.of(favorite.getSource()),
				StationResponse.of(favorite.getTarget())
		);
	}

	public static List<FavoriteResponse> ofList(List<Favorite> favorites) {
		return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
	}

	public long getId() {
		return id;
	}

	public StationResponse getSource() {
		return source;
	}

	public StationResponse getTarget() {
		return target;
	}
}
