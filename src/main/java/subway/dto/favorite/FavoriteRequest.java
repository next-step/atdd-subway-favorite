package subway.dto.favorite;

public class FavoriteRequest {
	private final Long source;
	private final Long target;

	public FavoriteRequest(Long source, Long target) {
		this.source = source;
		this.target = target;
	}

	public Long getSource() {
		return source;
	}

	public Long getTarget() {
		return target;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Long source;
		private Long target;

		Builder() {
		}

		public Builder source(Long source) {
			this.source = source;
			return this;
		}

		public Builder target(Long target) {
			this.target = target;
			return this;
		}

		public FavoriteRequest build() {
			return new FavoriteRequest(source, target);
		}
	}
}
