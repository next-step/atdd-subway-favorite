package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long memberId;

	@ManyToOne
	private Station source;

	@ManyToOne
	private Station target;

	@Builder
	public Favorite(Long memberId, Station source, Station target) {
		this.memberId = memberId;
		this.source = source;
		this.target = target;
	}

	public static Favorite of(Long memberid, Station source, Station target) {
		return Favorite.builder()
				.memberId(memberid)
				.source(source)
				.target(target)
				.build();
	}
}
