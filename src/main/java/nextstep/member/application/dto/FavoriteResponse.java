package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;

public class FavoriteResponse {
	private Long memberId;
	private Source source;
	private Target target;

	protected FavoriteResponse() {
	}

	public FavoriteResponse(Long memberId, Source source, Target target) {
		this.memberId = memberId;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(favorite.getMemberId(), new Source(favorite.getSource().getId(), favorite.getSource().getName()), new Target(favorite.getTarget().getId(), favorite.getTarget().getName()));
	}

	public Long getMemberId() {
		return memberId;
	}

	public Source getSource() {
		return source;
	}

	public Target getTarget() {
		return target;
	}

	static class Source {
		private Long id;
		private String name;

		public Source(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	static class Target {
		private Long id;
		private String name;

		public Target(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}
}
