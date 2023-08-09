package nextstep.favorite.domain;

import nextstep.line.domain.Line;
import nextstep.line.domain.PathFinder;
import nextstep.member.domain.Member;
import nextstep.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @ManyToOne
    private Station source;

    @ManyToOne
    private Station target;

    protected Favorite() {
    }

    public Favorite(Long memberId, Station source, Station target, PathFinder pathFinder) {
        pathFinder.validateStation(source, target);
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public boolean isOwner(Member member) {
        return this.memberId.equals(member.getId());
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
