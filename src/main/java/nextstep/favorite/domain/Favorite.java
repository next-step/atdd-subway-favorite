package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target")
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Favorite() {

    }
    private Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public static Favorite register(Station source, Station target, Member member) {
        return new Favorite(source, target, member);
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
