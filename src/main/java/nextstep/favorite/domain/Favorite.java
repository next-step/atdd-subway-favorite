package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_id")
    private Station source;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_id")
    private Station target;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    private Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    protected Favorite() {}

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public static Favorite create(Station source, Station target, Member member) {
        return new Favorite(source, target, member);
    }
}
