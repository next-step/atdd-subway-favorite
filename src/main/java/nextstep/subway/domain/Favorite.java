package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Favorite {
	@Id
	private Long id;

	@OneToOne
	@JoinColumn(name = "source_id")
	private Station source;

	@OneToOne
	@JoinColumn(name = "target_id")
	private Station target;

	public Favorite(Station source, Station target) {
		this.source = source;
		this.target = target;
	}

	protected Favorite() {

	}

	public Long getId() {
		return id;
	}

	public Station getSource() {
		return source;
	}

	public Station getTarget() {
		return target;
	}
}
