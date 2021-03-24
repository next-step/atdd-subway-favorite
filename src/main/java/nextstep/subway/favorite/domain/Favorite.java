package nextstep.subway.favorite.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private long memberId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite() {}

    public Favorite(long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
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

    public long getMemberId() {
        return memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        Favorite favorite = (Favorite) o;

        if (Objects.equals(id, favorite.id)) return true;

        return Objects.equals(memberId, favorite.memberId) &&
               Objects.equals(source, favorite.source) &&
               Objects.equals(target, favorite.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, source, target);
    }
}
