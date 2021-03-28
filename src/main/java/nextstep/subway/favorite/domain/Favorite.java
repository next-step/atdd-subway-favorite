package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "source")
    private Station source;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "target")
    private Station target;

    public Favorite() {
    }

    private Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Long memberId, Station source, Station target) {
        return new Favorite(memberId, source, target);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public boolean isOwner(long memberId) {
        return this.memberId == memberId;
    }
}
