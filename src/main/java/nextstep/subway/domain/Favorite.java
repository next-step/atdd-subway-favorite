package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Favorite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "source_id")
	private Station source;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "target_id")
	private Station target;

	public Favorite(Station source, Station target) {
		this.source = source;
		this.target = target;
	}

	public Favorite() {

	}

	public void setSource(Station source) {
		this.source = source;
	}

	public void setTarget(Station target) {
		this.target = target;
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
