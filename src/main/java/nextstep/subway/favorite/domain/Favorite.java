package nextstep.subway.favorite.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite() {}

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        Favorite favorite = (Favorite) o;

        if (Objects.equals(id, favorite.id)) return true;

        return Objects.equals(member, favorite.member) &&
               Objects.equals(source, favorite.source) &&
               Objects.equals(target, favorite.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, source, target);
    }
}
