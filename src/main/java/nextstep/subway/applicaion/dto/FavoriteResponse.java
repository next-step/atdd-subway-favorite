package nextstep.subway.applicaion.dto;

public class FavoriteResponse {
	private long id;
	private StationResponse source;
	private StationResponse target;

	public FavoriteResponse() {

	}

	public FavoriteResponse(long id, StationResponse source, StationResponse target) {
		this.id = id;
		this.source = source;
		this.target = target;
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
