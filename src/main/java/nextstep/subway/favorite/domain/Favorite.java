package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "source")
    private Station source;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "target")
    private Station target;

    public Favorite() {
    }

    public Favorite(Long id, Member member, Station source, Station target) {
        this.id = id;
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public Member getMember() {
        return member;
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

    public void updateMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(member, favorite.member) && Objects.equals(source, favorite.source) && Objects.equals(target, favorite.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, source, target);
    }

    public void update(Favorite entity) {
        this.member = entity.getMember();
        this.source = entity.getSource();
        this.target = entity.getTarget();
    }
}
