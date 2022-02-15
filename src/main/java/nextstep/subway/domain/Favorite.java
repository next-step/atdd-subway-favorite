package nextstep.subway.domain;

import nextstep.exception.SameStationException;
import nextstep.member.domain.Member;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "source_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station source;

    @JoinColumn(name = "target_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station target;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    protected Favorite() {
    }

    public static Favorite of(Station source, Station target, Member member) {
        return new Favorite(source, target, member);
    }

    private Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public void validateStations() {
        validateStations();
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

    public Member getMember() {
        return member;
    }
}
