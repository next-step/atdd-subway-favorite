package nextstep.subway.member.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "member_id")
    Long memberId;

    @ManyToOne
    @JoinColumn(name = "source_id")
    Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    Station target;

    public Favorite() {
    }

    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getMemberId() {
        return memberId;
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
