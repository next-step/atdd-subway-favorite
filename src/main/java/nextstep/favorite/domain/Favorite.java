package nextstep.favorite.domain;

import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source")
    private Station source;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target")
    private Station target;

    @Column(name = "member_id")
    private Long memberId;

    protected Favorite() {

    }

    private Favorite(Station source, Station target, Long memberId) {
        this.source = source;
        this.target = target;
        this.memberId = memberId;
    }

    public static Favorite register(Station source, Station target, Long memberId) {
        return new Favorite(source, target, memberId);
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
