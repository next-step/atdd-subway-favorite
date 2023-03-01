package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Entity
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source", referencedColumnName = "id")
    private Station source;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target", referencedColumnName = "id")
    private Station target;

    public Favorite(Station source, Station target) {
        this.source = source;
        this.target = target;
    }
}
