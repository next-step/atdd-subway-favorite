package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
	private Long id;
	private Source source;
	private Target target;

	protected FavoriteResponse() {
	}

	public FavoriteResponse(Long id, Source source, Target target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(favorite.getMemberId(), new Source(favorite.getSource().getId(), favorite.getSource().getName()), new Target(favorite.getTarget().getId(), favorite.getTarget().getName()));
	}

	public static List<FavoriteResponse> of(List<Favorite> favorite) {
		List<FavoriteResponse> collect = favorite.stream()
				.map(FavoriteResponse::of)
				.collect(Collectors.toList());
		return collect;
	}

	public Long getId() {
		return id;
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
