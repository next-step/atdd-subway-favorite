package nextstep.member.domain;

import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @Column(name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() { }

    private Favorite(Station source, Station target) {
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Station source, Station target) {
        return new Favorite(source, target);
    }

    public void addMember(Member member) {
        this.member = member;
    }

}
