package nextstep.subway.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;
    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite(final Station source, final Station target) {
        this.source = source;
        this.target = target;
    }
}
